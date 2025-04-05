package com.example.GestionProject.dto;

import com.example.GestionProject.model.RoleName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private RoleName role;
}
