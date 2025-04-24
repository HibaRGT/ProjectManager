package com.example.GestionProject.dto;

import com.example.GestionProject.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStoryDTO implements Serializable {

    @NotNull(message = "id may not be null")
    private Long id;

    @NotBlank(message = "titre may not be blank")
    private String titre;

    @NotBlank(message = "description may not be blank")
    private String description;

    private int valeurMetier;
    private int urgence;
    private int complexite;
    private int risques;
    private int dependances;

    @NotNull
    private StatutEnum statut;

    private Long epicId;

    private Long productBacklogId;

    private Long sprintBacklogId;

    private MoSCoWPriority priorite;

    @NotNull(message = "The list of taskIds may not be null")
    @Builder.Default
    private List<Long> taskIds = new ArrayList<>();
}
