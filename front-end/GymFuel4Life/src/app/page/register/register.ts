import { Component, OnInit } from '@angular/core';
import { RegisterRequest } from '../../models/register-request';
import { Auth } from '../../service/auth';
import { Router } from '@angular/router'; // Cleaned up unused 'Route'
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule], // Ensure these are here for your form to work
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register implements OnInit {
  showPassword = false;
  showConfirmPassword = false;
  registerRequest: RegisterRequest = {
    name: '',
    surname: '',
    emailAddress: '',
    password: '',
    confirmPassword: '',
    gender: '',
    dob: '',
  };

  fieldErrors: any = {};

  constructor(
    private authService: Auth,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const email = localStorage.getItem('emailStored');
    if (email) {
      this.authService.retrieveUser(email).subscribe({
        next: (data: RegisterRequest) => {
          // Prefill data from the Google temporary account
          this.registerRequest = { ...data };
          this.registerRequest.gender = data.gender || '';
        },
        error: (err) => console.error('Failed to pre-fill user data', err),
      });
    }
  }

  onRegisterSubmit() {
    this.fieldErrors = {};
    const isGoogleUser = localStorage.getItem('emailStored') !== null;

    if (isGoogleUser) {
      // GOOGLE PATH: Update existing and go to Dashboard
      this.authService.completeGoogleProfile(this.registerRequest).subscribe({
        next: (res: any) => {
          if (res.token) {
            localStorage.setItem('token', res.token); // Save the JWT for Dashboard access
          }
          localStorage.removeItem('emailStored');
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          if (err.status === 400) {
            this.fieldErrors = { ...err.error };
            console.log('Errors caught immediately:', this.fieldErrors);
          }
        },
      });
    } else {
      // MANUAL PATH: Create new and go to Login
      this.authService.register(this.registerRequest).subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (err) => {
          if (err.status === 400) {
            this.fieldErrors = { ...err.error };
            console.log('Errors caught immediately:', this.fieldErrors);
          }
        },
      });
    }
  }
}
