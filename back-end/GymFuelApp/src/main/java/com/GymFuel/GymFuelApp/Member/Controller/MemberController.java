package com.GymFuel.GymFuelApp.Member.Controller;

import com.GymFuel.GymFuelApp.Member.DTO.LoginMemberDTO;
import com.GymFuel.GymFuelApp.Member.DTO.RegisterUserDTO;
import com.GymFuel.GymFuelApp.Member.Serivce.MemberImp;
import com.GymFuel.GymFuelApp.Member.Serivce.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService=memberService;
    }

    @PostMapping("/RegisterUser")
    public void registerUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
            memberService.registerNewUser(registerUserDTO);

    }

    @PostMapping("/LoginUser")
    public String loginUser(@RequestBody @Valid LoginMemberDTO LoginMemberDTO) {
        return memberService.loginNewUser(LoginMemberDTO);

    }
}
