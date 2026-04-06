package com.GymFuel.GymFuelApp.Member.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginMemberDTO {
    @NotBlank(message = "username cannot be blank")
    private String email;
    @NotBlank(message = "password cannot be empty")
    private String password;
}
