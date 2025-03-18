package com.example.GestionProject.dto;

import com.example.GestionProject.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStoryDTO implements Serializable {

    @NotNull(message = "id may not be null")
    private Long id;

    @NotBlank(message = "titre may not be blank")
    private String titre;

    @NotBlank(message = "description may not be blank")
    private String description;

    private int priorite;

    @NotNull
    private StatutEnum statut;

    private Long epicId;

    private Long productBacklogId;

    private Long sprintBacklogId;

    @NotNull(message = "The list of taskIds may not be null")
    private List<Long> taskIds;
}
