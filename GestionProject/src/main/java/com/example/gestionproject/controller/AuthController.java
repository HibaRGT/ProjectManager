

package com.example.gestionproject.controller;

import com.example.gestionproject.dto.*;
import com.example.gestionproject.service.implementation.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);
        log.info("Generated JWT: {}", authResponse.getToken());
        return authResponse;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/logout")
    public String logout() {
        return "User logged out successfully. Please remove token from client storage.";
    }

}
