import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { DashboardRequest } from '../../models/dashboard-request';
import { Auth } from '../../service/auth';
import { Dashboard } from '../dashboard/dashboard';
import { DashboardService } from '../../service/dashboard-service';

@Component({
  selector: 'app-dashboard-welcome',
  imports: [],
  templateUrl: './dashboard-welcome.html',
  styleUrl: './dashboard-welcome.css',
})
export class DashboardWelcome {
  dashboardData: any = null;
  isLoading = true;

  constructor(
    private dashboardService: DashboardService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    // The Welcome page pulls the data again to be "independent"
    this.dashboardService.dashboard().subscribe({
      next: (res) => {
        this.dashboardData = res;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Data pull failed', err);
        this.isLoading = false;
      },
    });
  }

  goToGymSetup() {
    this.router.navigate(['/gym-setup']);
  }

  goToDietSetup() {
    this.router.navigate(['/diet-setup']);
  }
}
