package com.example.GestionProject.dto;

import com.example.GestionProject.model.ProductBacklog;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO implements Serializable {

    private Long id;
    private String nom;
    private String description;
    private Long productBacklogId;
}
