package com.GymFuel.GymFuelApp.Member.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "surname cannot be empty")
    private String surname;
    @NotBlank(message = "Gender cannot be empty")
    private String gender;
    @NotBlank(message = "DOB cannot be empty")
    private String dob;
    @Email(message = "Please provide a real email format")
    @NotBlank(message = "must be valid or email address already exist")
    private String emailAddress;
    @NotBlank(message = "password cannot be empty and size must be between 9 - 20")
    @Size(min = 9, max = 20)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;
    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;
    @Override
    public String toString() {
        return "RegisterUserDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", height=" + emailAddress +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }

}
