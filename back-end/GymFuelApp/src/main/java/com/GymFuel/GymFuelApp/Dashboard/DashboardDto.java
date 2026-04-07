package com.GymFuel.GymFuelApp.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private String firstName;
    private boolean gymSetupComplete;
    private boolean dietSetupComplete;
}
