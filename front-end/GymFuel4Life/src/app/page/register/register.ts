import { Component, OnInit } from '@angular/core';
import { RegisterRequest } from '../../models/register-request';
import { Auth } from '../../service/auth';
import { Route, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register implements OnInit {
  constructor(
    private authService: Auth,
    private router: Router,
  ) {}

  registerRequest: RegisterRequest = {
    name: '',
    surname: '',
    emailAddress: '',
    password: '',
    confirmPassword: '',
    gender: '',
    dob: '',
  };

  ngOnInit(): void {
    const email = localStorage.getItem('emailStored');
    if (email) {
      this.authService.retrieveUser(email).subscribe({
        next: (data: RegisterRequest) => {
          this.registerRequest.name = data.name;
          this.registerRequest.surname = data.surname;
          this.registerRequest.emailAddress = data.emailAddress;
          this.registerRequest.password = data.password;
          this.registerRequest.confirmPassword = data.confirmPassword;
          this.registerRequest.gender = data.gender;
          this.registerRequest.dob = data.dob;

          this.registerRequest.gender = data.gender || '';
        },
        error: (err) =>
          console.error('Failed to get user data from token', err),
      });
    }
  }

  fieldErrors: any = {};
  showPassword = false;
  showConfirmPassword = false;

  onRegisterSubmit() {
    alert(this.registerRequest.dob);
    this.fieldErrors = {};

    //remeber we used the existing email came that from google and if
    //blank its comes from manual setup
    const isGoogleUser = localStorage.getItem('emailStored') !== null;
    alert(isGoogleUser);

    if (isGoogleUser) {
      // Google user — update existing record
      this.authService.completeGoogleProfile(this.registerRequest).subscribe({
        next: (res) => {
          console.log('Success:', res);
          localStorage.removeItem('emailStored');
          this.router.navigate(['/login']);
          console.log('====================================');
          console.log('test');
          console.log('====================================');
        },
        error: (err) => {
          console.log('Success:', err);
          if (err.status === 400) {
            this.fieldErrors = err.error;
            console.log('====================================');
            console.log('test2');
            console.log('====================================');
          }
        },
      });
    } else {
      // Manual user — create new record
      this.authService.register(this.registerRequest).subscribe({
        next: (res) => {
          this.router.navigate(['/login']);
        },
        error: (err) => {
          if (err.status === 400) this.fieldErrors = err.error;
        },
      });
    }
  }
}
