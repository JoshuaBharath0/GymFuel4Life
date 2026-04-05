package com.GymFuel.GymFuelApp.Member.Serivce;

import com.GymFuel.GymFuelApp.Member.DTO.LoginMemberDTO;
import com.GymFuel.GymFuelApp.Member.DTO.RegisterUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface MemberService {
    void registerNewUser(RegisterUserDTO registerUserDTO);
    String loginNewUser(LoginMemberDTO loginMemberDTO);
    ResponseEntity<?> processGoogleLogin(String token);
    ResponseEntity<?> findMemberByEmail(String email);
}
