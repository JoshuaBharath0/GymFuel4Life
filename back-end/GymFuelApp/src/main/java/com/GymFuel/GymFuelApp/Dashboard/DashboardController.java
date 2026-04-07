package com.GymFuel.GymFuelApp.Dashboard;

import com.GymFuel.GymFuelApp.Dashboard.Service.DashboardService;
import com.GymFuel.GymFuelApp.Member.Serivce.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> getDashboard(Principal principal) {
        // Principal gets the email of the logged-in user automatically
        String email = principal.getName();
        return ResponseEntity.ok(dashboardService.getDashboardData(email));
    }

}
