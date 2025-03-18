package com.example.GestionProject.dto;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.UserStory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpicDTO implements Serializable {

    private Long id;
    private String nom;
    private String description;
    private Long productBacklogId;
    private List<Long> userStoryIds;
}
