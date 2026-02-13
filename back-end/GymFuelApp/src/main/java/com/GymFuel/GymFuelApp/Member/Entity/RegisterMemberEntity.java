package com.GymFuel.GymFuelApp.Member.Entity;

import com.GymFuel.GymFuelApp.Member.DTO.RegisterUserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "members")
@NoArgsConstructor
public class RegisterMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-generates user_id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "first_name")
    private String name;
    @Column(name = "last_name")
    private String surname;
    @Column(name = "gender")
    private String gender;
    @Column(name = "date_of_birth")
    private LocalDate DOB;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    public RegisterMemberEntity(RegisterUserDTO dto) {
        this.name = dto.getName();
        this.surname = dto.getSurname();
        this.gender = dto.getGender();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.DOB = LocalDate.parse(dto.getDOB());
        this.emailAddress = dto.getEmailAddress();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
    }

}
