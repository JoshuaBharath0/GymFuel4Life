import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../models/login-request';
import { Observable } from 'rxjs';
import { RegisterRequest } from '../models/register-request';
import { environment } from '../../environments/environment';
import { DashboardRequest } from '../models/dashboard-request';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly api_url = environment.apiUrl + '/member';
  constructor(private http: HttpClient) {}

  login(loginData: LoginRequest): Observable<string> {
    return this.http.post(`${this.api_url}/LoginUser`, loginData, {
      responseType: 'text',
    });
  }

  register(registerRequest: RegisterRequest): Observable<any> {
    return this.http.post<any>(`${this.api_url}/RegisterUser`, registerRequest);
  }

  loginWithGoogle(token: string) {
    return this.http.post(`${this.api_url}/GoogleLogin`, { token: token });
  }

  retrieveUser(email: string): Observable<RegisterRequest> {
    return this.http.get<RegisterRequest>(
      `${this.api_url}/retrieveUser?email=${email}`,
    );
  }
  completeGoogleProfile(registerRequest: RegisterRequest): Observable<any> {
    console.log('Full request:', JSON.stringify(registerRequest));

    return this.http.put<any>(
      `${this.api_url}/completeProfile`,
      registerRequest,
    );
  }

  verifyToken(token: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.api_url}/verify`, {
      headers: { Authorization: `Bearer ${token}` },
    });
  }

  dashboard(): Observable<DashboardRequest> {
    return this.http.get<DashboardRequest>(`${this.api_url}/dashboard`, {
      withCredentials: true,
    });
  }
}
