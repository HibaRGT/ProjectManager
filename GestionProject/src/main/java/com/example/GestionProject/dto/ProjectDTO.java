package com.example.GestionProject.dto;

import com.example.GestionProject.model.ProductBacklog;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDTO implements Serializable {

    @NotNull(message = "id may not be null")
    private Long id;

    @NotBlank(message = "nom may not be blank")
    private String nom;

    @NotBlank(message = "description may not be blank")
    private String description;

    private Long productBacklogId;
}
