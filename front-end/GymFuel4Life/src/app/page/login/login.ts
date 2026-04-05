import { Component, OnInit } from '@angular/core';
import { LoginRequest } from '../../models/login-request';
import { FormsModule } from '@angular/forms';
import { Auth } from '../../service/auth';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

declare var google: any;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  loginData: LoginRequest = {
    username: '',
    password: '',
  };

  constructor(
    private authService: Auth,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const checkGoogle = setInterval(() => {
      if (typeof google !== 'undefined') {
        clearInterval(checkGoogle);
        this.initializeGoogle();
      }
    }, 100);
  }

  initializeGoogle(): void {
    google.accounts.id.initialize({
      client_id:
        '346730978765-6ta6iuel5d9lipkmv4f2e0hfgm1cc32s.apps.googleusercontent.com',
      callback: (response: any) => this.handleGoogleResponse(response),
      use_fedcm_for_prompt: false,
      ux_mode: 'popup',
    });

    // Render into hidden div so SDK is ready, button click triggers it
    google.accounts.id.renderButton(document.getElementById('g_id_signin'), {
      theme: 'outline',
      size: 'large',
    });
  }

  signInWithGoogle(): void {
    const hiddenDiv = document.getElementById('g_id_signin');
    if (hiddenDiv) {
      const btn = hiddenDiv.querySelector('div[role=button]') as HTMLElement;
      if (btn) btn.click();
    }
  }

  private handleGoogleResponse(response: any): void {
    const idToken = response.credential;

    this.authService.loginWithGoogle(idToken).subscribe({
      next: (res: any) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('emailStored', res.email);
        this.router.navigate(
          res.isProfileComplete ? ['/dashboard'] : ['/register'],
        );
      },
      error: (err) => console.error('Google Login Error:', err),
    });
  }

  onLoginSubmit(): void {
    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        alert('Success: ' + response);
      },
      error: (err) => {
        console.error('Login Error:', err);
        alert('Login Failed');
      },
    });
  }
}
