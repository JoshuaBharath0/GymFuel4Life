package com.GymFuel.GymFuelApp.Dashboard;

import com.GymFuel.GymFuelApp.Member.Entity.RegisterMemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_portal_state")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link this to your existing User entity
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private RegisterMemberEntity user;

    private boolean gymSetupComplete = false;
    private boolean dietSetupComplete = false;
}
