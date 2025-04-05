package com.example.GestionProject.service.implementation;

import com.example.GestionProject.dto.*;
import com.example.GestionProject.model.*;
import com.example.GestionProject.repository.*;
import com.example.GestionProject.config.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthResponse register(RegisterRequest request) {
        // Use the RoleName enum directly
        RoleName roleName = request.getRole();

        // Create the User with the RoleName
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleName)
                .build();
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        ;
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtils.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
