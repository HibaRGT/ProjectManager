package com.example.GestionProject.dto;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.Sprint;
import com.example.GestionProject.model.UserStory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintBacklogDTO implements Serializable {
    private Long id;
    private Long sprintId;
    private List<Long> userStoryIds;
    private Long productBacklogId;
}
