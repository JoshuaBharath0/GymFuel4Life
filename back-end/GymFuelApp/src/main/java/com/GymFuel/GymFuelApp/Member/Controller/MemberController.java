package com.GymFuel.GymFuelApp.Member.Controller;

import com.GymFuel.GymFuelApp.Configuration.JwtService;
import com.GymFuel.GymFuelApp.Member.DTO.GoogleTokenDTO;
import com.GymFuel.GymFuelApp.Member.DTO.LoginMemberDTO;
import com.GymFuel.GymFuelApp.Member.DTO.RegisterUserDTO;
import com.GymFuel.GymFuelApp.Member.Entity.RegisterMemberEntity;
import com.GymFuel.GymFuelApp.Member.Serivce.MemberImp;
import com.GymFuel.GymFuelApp.Member.Serivce.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;


@RestController
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;
    private JwtService jwtService;

    @Autowired
    public MemberController(MemberService memberService, JwtService jwtService) {
        this.memberService = memberService;
        this.jwtService = jwtService;
    }

    @PutMapping("/completeProfile")
    public ResponseEntity<?> completeProfile(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        System.out.println(registerUserDTO.toString());

        return memberService.completeGoogleUserProfile(registerUserDTO);
    }

    @PostMapping("/RegisterUser")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        memberService.registerNewUser(registerUserDTO);
        return ResponseEntity.ok(Map.of("message", "User is Registered successfully"));
    }

    @PostMapping("/LoginUser")
    public String loginUser(@RequestBody @Valid LoginMemberDTO LoginMemberDTO) {
        return memberService.loginNewUser(LoginMemberDTO);

    }

    @PostMapping("/GoogleLogin")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleTokenDTO googleTokenDTO) {
        return memberService.processGoogleLogin(googleTokenDTO.getToken());
    }

    @GetMapping("/retrieveUser")
    public ResponseEntity<?> retrieveUser(@RequestParam String email) {
        return memberService.findMemberByEmail(email);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("valid", false));
        }

        try {
            // 1. Get the raw token
            String token = authHeader.substring(7);

            // 2. Use the injected instance to extract the email
            String email = jwtService.extractEmail(token);

            // 3. Check database
            boolean exists = memberService.checkUserExists(email);

            if (exists) {
                return ResponseEntity.ok(Map.of("valid", true));
            } else {
                return ResponseEntity.status(401).body(Map.of("valid", false));
            }
        } catch (Exception e) {
            // If token is expired or malformed, it will throw an exception
            return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Invalid token"));
        }
    }
}
