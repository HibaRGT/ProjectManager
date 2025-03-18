package com.example.GestionProject.dto;

import com.example.GestionProject.model.TaskStatus;
import com.example.GestionProject.model.UserStory;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO implements Serializable {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;

    private Long userStoryId;

}
