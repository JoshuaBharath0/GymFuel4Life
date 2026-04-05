package com.GymFuel.GymFuelApp.Member.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponseDTO {
    private String token;
    private boolean isProfileComplete;
    private String email;

    public AuthResponseDTO(String token, boolean isProfileComplete,String email) {
        this.token = token;
        this.isProfileComplete = isProfileComplete;
        this.email=email;
    }

    public String getToken() { return token; }

    // Rename getter to force Jackson to serialize as "isProfileComplete"
    @JsonProperty("isProfileComplete")
    public boolean isProfileComplete() { return isProfileComplete; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}