package com.example.GestionProject.dto;

import com.example.GestionProject.model.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryDTO implements Serializable {

    private Long id;
    private String titre;
    private String description;
    private int priorite;
    private StatutEnum statut;
    private Long epicId;
    private ProductBacklog productBacklog;
    private Long sprintBacklogId;
    private List<Long> taskIds;
}
