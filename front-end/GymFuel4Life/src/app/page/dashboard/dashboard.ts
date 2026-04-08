import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Auth } from '../../service/auth';
import { DashboardWelcome } from '../dashboard-welcome/dashboard-welcome';
import { DashboardMain } from '../dashboard-main/dashboard-main'; // Your future real dashboard
import { DashboardService } from '../../service/dashboard-service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, DashboardWelcome, DashboardMain],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  dashboardData: any = null;
  isLoading = true;

  constructor(private authService: DashboardService) {}

  ngOnInit(): void {
    // This calls your Java method: getDashboardData(email)
    this.authService.dashboard().subscribe({
      next: (data) => {
        this.dashboardData = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }
}
