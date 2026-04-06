import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Auth } from '../../service/auth';
import { LoginRequest } from '../../models/login-request';
import { environment } from '../../../environments/environment';

declare var google: any;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit, OnDestroy {
  // --- Data ---
  loginData: LoginRequest = { email: '', password: '' };
  private readonly googleApi = environment.googleApi;
  private scriptCheckInterval: any;

  constructor(
    private authService: Auth,
    private router: Router,
  ) {}

  // --- Life Cycle (Start & Stop) ---

  ngOnInit(): void {
    this.startGoogleService();
  }

  ngOnDestroy(): void {
    this.cleanupInterval();
  }

  // --- Google Integration Logic ---

  private startGoogleService(): void {
    // If Google is already loaded in the browser, just show the button
    if ((window as any).google_initialized) {
      this.renderGoogleButton();
      return;
    }

    // Otherwise, wait for the script to be ready
    this.scriptCheckInterval = setInterval(() => {
      if (typeof google !== 'undefined' && google.accounts?.id) {
        this.cleanupInterval();

        if (!(window as any).google_initialized) {
          this.setupGoogleIdentity();
        } else {
          this.renderGoogleButton();
        }
      }
    }, 100);
  }

  private setupGoogleIdentity(): void {
    (window as any).google_initialized = true;

    google.accounts.id.initialize({
      client_id: this.googleApi,
      callback: (res: any) => this.handleGoogleResponse(res),
      use_fedcm_for_prompt: false,
      ux_mode: 'popup',
    });

    this.renderGoogleButton();
  }

  renderGoogleButton(): void {
    const btnElement = document.getElementById('g_id_signin');
    if (btnElement) {
      google.accounts.id.renderButton(btnElement, {
        theme: 'outline',
        size: 'large',
      });
    }
  }

  // --- User Actions (Button Clicks) ---

  // Standard Login
  onLoginSubmit(): void {
    this.authService.login(this.loginData).subscribe({
      next: (token: string) => {
        localStorage.setItem('token', token);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Login Error:', err);
        alert('Oops! Check your email or password.');
      },
    });
  }

  // Google Login Trigger
  signInWithGoogle(): void {
    const googleBtnWrapper = document.getElementById('g_id_signin');
    const innerBtn = googleBtnWrapper?.querySelector(
      'div[role="button"]',
    ) as HTMLElement;

    if (innerBtn) {
      innerBtn.click();
    } else {
      google.accounts.id.prompt(); // Fallback to the one-tap prompt
    }
  }

  // New User Navigation
  registerUser(): void {
    localStorage.clear(); // Fresh start
    this.router.navigate(['register']);
  }

  // --- Helpers ---

  private handleGoogleResponse(response: any): void {
    this.authService.loginWithGoogle(response.credential).subscribe({
      next: (res: any) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('emailStored', res.email);

        // Send them where they belong
        const target = res.isProfileComplete ? '/dashboard' : '/register';
        this.router.navigate([target]);
      },
      error: (err) => console.error('Google Auth Failed:', err),
    });
  }

  private cleanupInterval(): void {
    if (this.scriptCheckInterval) {
      clearInterval(this.scriptCheckInterval);
    }
  }
}
//clean=====================================================================================
