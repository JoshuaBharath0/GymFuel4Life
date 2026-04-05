package com.GymFuel.GymFuelApp.Member.Controller;

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

@CrossOrigin(origins = "http://localhost:4200" , allowCredentials = "true")
@RestController
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService=memberService;
    }

    @PostMapping("/RegisterUser")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
            memberService.registerNewUser(registerUserDTO);
           return   ResponseEntity.ok("User is Registered successfully ");

    }

    @PostMapping("/LoginUser")
    public String loginUser(@RequestBody @Valid LoginMemberDTO LoginMemberDTO) {
        return memberService.loginNewUser(LoginMemberDTO);

    }
    @PostMapping("/GoogleLogin")
    public ResponseEntity<?>googleLogin(@RequestBody GoogleTokenDTO googleTokenDTO){
        return memberService.processGoogleLogin(googleTokenDTO.getToken());
    }
 @GetMapping("/retrieveUser")
    public ResponseEntity<?> retrieveUser(@RequestParam String email){
        return memberService.findMemberByEmail(email);
 }
}
