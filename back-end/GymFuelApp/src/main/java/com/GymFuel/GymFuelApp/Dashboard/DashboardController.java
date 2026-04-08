package com.GymFuel.GymFuelApp.Dashboard;

import com.GymFuel.GymFuelApp.Dashboard.Service.DashboardService;
import com.GymFuel.GymFuelApp.Member.Serivce.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping()
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
        System.out.println(email);
        return ResponseEntity.ok(dashboardService.getDashboardData(email));
    }

}
