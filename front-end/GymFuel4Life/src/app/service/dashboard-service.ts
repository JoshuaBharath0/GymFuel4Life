import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { DashboardRequest } from '../models/dashboard-request';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private readonly api_url = environment.apiUrl;
  constructor(private http: HttpClient) {}
  dashboard(): Observable<DashboardRequest> {
    return this.http.get<DashboardRequest>(`${this.api_url}/dashboard`, {
      withCredentials: true,
    });
  }
}
