package com.GymFuel.GymFuelApp.Member.Serivce;

import com.GymFuel.GymFuelApp.Member.DAO.MemberRepository;
import com.GymFuel.GymFuelApp.Member.DTO.LoginMemberDTO;
import com.GymFuel.GymFuelApp.Member.DTO.RegisterUserDTO;
import com.GymFuel.GymFuelApp.Member.Entity.RegisterMemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class MemberImp implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MemberImp(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void registerNewUser(RegisterUserDTO registerUserDTO) {

        if (!registerUserDTO.getPassword().equals(registerUserDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match!");
        }

        LocalDate DOB = LocalDate.parse(registerUserDTO.getDOB());
        LocalDate currentYear = LocalDate.now();
        int age = Period.between(DOB, currentYear).getYears();

        if (memberRepository.findExistingMemer(registerUserDTO.getUsername())) {
            throw new IllegalArgumentException("Username '" + registerUserDTO.getUsername() + "' already exist");
        }
        if (memberRepository.emailExists(registerUserDTO.getEmailAddress())) {
            throw new IllegalArgumentException("Email '" + registerUserDTO.getEmailAddress() + "' already exist");
        }
        if (!isDomainValid(registerUserDTO.getEmailAddress())) {
            throw new IllegalArgumentException("Email domain does not exist!");
        }

        if (age < 18) {
            throw new IllegalArgumentException("Too young to use this app");
        }

        if (!checkNullValues(
                registerUserDTO.getName(),
                registerUserDTO.getSurname(),
                registerUserDTO.getDOB(),
                registerUserDTO.getUsername(),
                registerUserDTO.getPassword()
        )) {

            String encryptedPassword = bCryptPasswordEncoder.encode(registerUserDTO.getPassword());
            registerUserDTO.setPassword(encryptedPassword);
            RegisterMemberEntity registerMemberEntity = new RegisterMemberEntity(registerUserDTO);
            memberRepository.registerMeber(registerMemberEntity);
        } else {
            throw new IllegalArgumentException("Required fields cannot be null");
        }

    }

    @Override
    public String loginNewUser(LoginMemberDTO loginMemberDTO) {
        RegisterMemberEntity member = memberRepository.findMemberByUsername(loginMemberDTO.getUsername());
        if (member == null) {
            throw new IllegalArgumentException("Invalid Username or Password");
        }
        boolean isMatch = bCryptPasswordEncoder.matches(loginMemberDTO.getPassword(), member.getPassword());
        if (!isMatch) {
            throw new IllegalArgumentException("Invalid Username or Password");
        }
        return "Login Successful! Welcome back, " + member.getUsername();
    }

    public boolean checkNullValues(Object... fieldValues) {
        for (Object value : fieldValues) {
            if (value == null) {
                return true;
            }
        }
        return false;
    }

    private boolean isDomainValid(String email) {
        try {
            String domain = email.substring(email.indexOf('@') + 1);
            org.xbill.DNS.Lookup lookup = new org.xbill.DNS.Lookup(domain, org.xbill.DNS.Type.MX);
            lookup.run();
            return lookup.getResult() == org.xbill.DNS.Lookup.SUCCESSFUL;
        } catch (Exception e) {
            return false;
        }

    }
}
