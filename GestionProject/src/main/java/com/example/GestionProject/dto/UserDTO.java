package com.example.GestionProject.dto;

import com.example.GestionProject.model.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotNull(message = "Username cannot be null")
    private String username;

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Role cannot be null")
    private RoleName role;
}
