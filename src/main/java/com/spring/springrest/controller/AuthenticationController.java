package com.spring.springrest.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.springrest.dto.AuthLoginDto;
import com.spring.springrest.dto.AuthRoleRegisterDto;
import com.spring.springrest.dto.AuthUserRegisterDto;
import com.spring.springrest.entity.AuthRole;
import com.spring.springrest.entity.AuthUser;
import com.spring.springrest.repository.AuthRoleRepository;
import com.spring.springrest.repository.AuthUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private AuthUserRepository authUserRepository;
    private AuthRoleRepository authRoleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    
    public AuthenticationController(AuthenticationManager authenticationManager,
            AuthUserRepository authUserRepository,
            AuthRoleRepository authRoleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        //super();
        this.authenticationManager = authenticationManager;
        this.authUserRepository = authUserRepository;
        this.authRoleRepository = authRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/register_authorized_role")
    public ResponseEntity<String> registerAuthorizedRole(
            @RequestBody AuthRoleRegisterDto authRoleRegisterDto) {
        if (authRoleRepository.existsByName(authRoleRegisterDto.getName())) {
            return new ResponseEntity<>(authRoleRegisterDto.getName() + "Role already exists", HttpStatus.BAD_REQUEST);
        }
        
        AuthRole authRole = new AuthRole();
        authRole.setName(authRoleRegisterDto.getName());
        
        authRoleRepository.save(authRole);
        return new ResponseEntity<>(authRole.getName() + " Role registered successfully", HttpStatus.CREATED);
    }
    
    @PostMapping("/register_authorized_user")
    public ResponseEntity<String> registerAuthorizedUser(
            @RequestBody AuthUserRegisterDto authUserRegisterDto) {
        // Implement user registration logic here
        if (authUserRepository
                .existsByUsername(authUserRegisterDto.getUsername())) {
            return new ResponseEntity<>(authUserRegisterDto.getUsername() + " username already exists",
                    HttpStatus.BAD_REQUEST);
        }
        
        AuthUser authUser = new AuthUser();
        authUser.setUsername(authUserRegisterDto.getUsername());
        authUser.setPassword(passwordEncoder.encode(authUserRegisterDto.getPassword()));
        
        AuthRole authRole = authRoleRepository.findByName("USER")
                .orElseThrow(
                        () -> new RuntimeException("Default role not found"));
        authUser.setRoles(Collections.singletonList(authRole));
        
        authUserRepository.save(authUser);
        
        return new ResponseEntity<>(authUser.getUsername() + " user registered successfully",
                HttpStatus.OK);
    }
    
    @PostMapping("/auth_login")
    public ResponseEntity<String> login(
            @RequestBody AuthLoginDto authLoginDto) {
        // Implement login logic here
        // You can use the authenticationManager to authenticate the user
        // and return a JWT token or any other response as needed.
        //return new ResponseEntity<>("Login successful", HttpStatus.OK);
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authLoginDto.getUsername(),
                        authLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        
        return new ResponseEntity<>("Login successful." , HttpStatus.OK);
    }
    
    @PostMapping("/auth_login_session")
    public ResponseEntity<?> loginWithSession(
            @RequestBody AuthLoginDto authLoginDto,
            HttpServletRequest request) {
        // Implement login logic here
        // You can use the authenticationManager to authenticate the user
        // and return a JWT token or any other response as needed.
        // return new ResponseEntity<>("Login successful", HttpStatus.OK);
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authLoginDto.getUsername(),
                        authLoginDto.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful with session.");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Logout successful.");
    }
}
