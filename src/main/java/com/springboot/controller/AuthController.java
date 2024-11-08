package com.springboot.controller;

import com.springboot.dto.LoginRequest;
import com.springboot.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.user(), loginRequest.password()));

            if ("user".equals(loginRequest.user()) && "password".equals(loginRequest.password())) {
                return ResponseEntity.ok(JwtTokenUtil.generateToken(loginRequest.user(), "USER"));
            } else if ("admin".equals(loginRequest.user()) && "password".equals(loginRequest.password())) {
                return ResponseEntity.ok(JwtTokenUtil.generateToken(loginRequest.user(), "ADMIN"));
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
