import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../models/login-request';
import { Observable } from 'rxjs';
import { RegisterRequest } from '../models/register-request';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly api_url = 'http://localhost:8080/member';
  constructor(private http: HttpClient) {}

  login(loginData: LoginRequest): Observable<String> {
    return this.http.post(`${this.api_url}/LoginUser`, loginData, {
      responseType: 'text',
    });
  }

  register(registerRequest: RegisterRequest): Observable<any> {
    alert('test');
    return this.http.post<any>(`${this.api_url}/RegisterUser`, registerRequest);
  }

  loginWithGoogle(token: string) {
    return this.http.post(`${this.api_url}/GoogleLogin`, { token: token });
  }

  retrieveUser(email: string): Observable<RegisterRequest> {
    return this.http.get<RegisterRequest>(
      `http://localhost:8080/member/retrieveUser?email=${email}`,
    );
  }
}
