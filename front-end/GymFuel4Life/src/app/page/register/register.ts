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
    username: '',
    emailAddress: '',
    password: '',
    confirmPassword: '',
    gender: '',
    DOB: '',
  };

  ngOnInit(): void {
    const email = localStorage.getItem('emailStored') ?? '';
    this.authService.retrieveUser(email).subscribe({
      next: (data: RegisterRequest) => {
        this.registerRequest.name = data.name;
        this.registerRequest.surname = data.surname;
        this.registerRequest.emailAddress = data.emailAddress;
        this.registerRequest.username = data.username;
        this.registerRequest.password = data.password;
        this.registerRequest.confirmPassword = data.confirmPassword;
        this.registerRequest.gender = data.gender;
        this.registerRequest.DOB = data.DOB;

        this.registerRequest.gender = data.gender || '';
      },
      error: (err) => console.error('Failed to get user data from token', err),
    });
  }

  fieldErrors: any = {}; // This holds our "Map" of errors

  onRegisterSubmit() {
    this.fieldErrors = {}; // Reset errors on every click

    this.authService.register(this.registerRequest).subscribe({
      next: (res) => {
        console.log('Success!', res);
        this.router.navigate(['/login']);
      },
      error: (err) => {
        if (err.status === 400) {
          this.fieldErrors = err.error;
        }
      },
    });
  }
}
