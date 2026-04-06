package com.GymFuel.GymFuelApp.Member.Serivce;

import com.GymFuel.GymFuelApp.Configuration.JwtService;
import com.GymFuel.GymFuelApp.Member.DAO.MemberRepository;
import com.GymFuel.GymFuelApp.Member.DTO.AuthResponseDTO;
import com.GymFuel.GymFuelApp.Member.DTO.LoginMemberDTO;
import com.GymFuel.GymFuelApp.Member.DTO.RegisterUserDTO;
import com.GymFuel.GymFuelApp.Member.Entity.RegisterMemberEntity;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.Map;

@Service
public class MemberImp implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtService jwtService;

    // Reuse verifier — building it is expensive
    private final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
            .Builder(new NetHttpTransport(), new GsonFactory())
            .setAudience(Collections.singletonList(
                    "346730978765-6ta6iuel5d9lipkmv4f2e0hfgm1cc32s.apps.googleusercontent.com"
            ))
            .build();

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

        LocalDate DOB = LocalDate.parse(registerUserDTO.getDob());
        int age = Period.between(DOB, LocalDate.now()).getYears();


        if (memberRepository.emailExists(registerUserDTO.getEmailAddress())) {
            throw new IllegalArgumentException("Email '" + registerUserDTO.getEmailAddress() + "' already exists");
        }
        if (!isDomainValid(registerUserDTO.getEmailAddress())) {
            throw new IllegalArgumentException("Email domain does not exist!");
        }
        if (age < 18) {
            throw new IllegalArgumentException("Too young to use this app");
        }
        if (checkNullValues(
                registerUserDTO.getName(),
                registerUserDTO.getSurname(),
                registerUserDTO.getDob(),
                registerUserDTO.getPassword()
        )) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }

        String encryptedPassword = bCryptPasswordEncoder.encode(registerUserDTO.getPassword());
        registerUserDTO.setPassword(encryptedPassword);
        memberRepository.registerMeber(new RegisterMemberEntity(registerUserDTO));
    }

    @Override
    public String loginNewUser(LoginMemberDTO loginMemberDTO) {
        RegisterMemberEntity member = memberRepository.findMemberByEmail(loginMemberDTO.getEmail());
        if (member == null) {
            throw new IllegalArgumentException("Invalid Username or Password");
        }
        if (!bCryptPasswordEncoder.matches(loginMemberDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Invalid Username or Password");
        }
        return  jwtService.generateToken(member.getEmailAddress());
    }

    @Override
    public ResponseEntity<?> findMemberByEmail(String email) {
        RegisterMemberEntity member = memberRepository.findMemberByEmail(email);
        if (member == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(member);
    }

    @Override
    public ResponseEntity<?> completeGoogleUserProfile(RegisterUserDTO registerUserDTO) {
        try {
            RegisterMemberEntity existingMember = memberRepository.findMemberByEmail(registerUserDTO.getEmailAddress());

            if (existingMember == null) {
                // Returning JSON error instead of string
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }

            // Update fields
            existingMember.setName(registerUserDTO.getName());
            existingMember.setSurname(registerUserDTO.getSurname());
            existingMember.setGender(registerUserDTO.getGender());
            existingMember.setDob(LocalDate.parse(registerUserDTO.getDob()));
            existingMember.setPassword(bCryptPasswordEncoder.encode(registerUserDTO.getPassword()));

            memberRepository.updateMember(existingMember);

            // Returning JSON success object
            return ResponseEntity.ok(Map.of("message", "Profile completed successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> processGoogleLogin(String token) {
        try {
            System.out.println("Received token (first 20 chars): " + token.substring(0, Math.min(20, token.length())));

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null) {
                System.out.println("Token verification failed — token was null after verify()");
                return ResponseEntity.status(401).body("Invalid or expired Google token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");

            System.out.println("Google login for: " + email);

            RegisterMemberEntity existingMember = memberRepository.findMemberByEmail(email);

            if (existingMember != null) {
                if(existingMember.getDob()!=null && existingMember.getEmailAddress() !=null){
                    String appToken = jwtService.generateToken(email); // TODO: replace with real JWT logic
                    boolean isComplete = (existingMember.getGender() != null && existingMember.getDob() != null);
                    return ResponseEntity.ok(new AuthResponseDTO(appToken, isComplete,existingMember.getEmailAddress()));
                }else{

                    String appToken = jwtService.generateToken(email); // TODO: replace with real JWT logic
                    return ResponseEntity.ok(new AuthResponseDTO(appToken, false,email));
                }
            } else {
                RegisterMemberEntity newMember = new RegisterMemberEntity();
                newMember.setEmailAddress(email);
                newMember.setName(firstName);
                newMember.setSurname(lastName);
                newMember.setPassword(null);
                memberRepository.registerMeber(newMember);

                String appToken = jwtService.generateToken(email); // TODO: replace with real JWT logic
                return ResponseEntity.ok(new AuthResponseDTO(appToken, false,email));
            }

        } catch (Exception e) {
            System.err.println("Google login exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server Error: " + e.getMessage());
        }
    }

    public boolean checkNullValues(Object... fieldValues) {
        for (Object value : fieldValues) {
            if (value == null) return true;
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