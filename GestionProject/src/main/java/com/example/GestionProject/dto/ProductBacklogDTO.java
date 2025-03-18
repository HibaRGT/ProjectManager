package com.example.GestionProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductBacklogDTO implements Serializable {

    @NotNull(message = "id may not be null")
    private Long id;

    @NotBlank(message = "nom may not be blank")
    private String nom;

    @NotBlank(message = "description may not be blank")
    private String description;

    @NotNull(message = "List of epic Ids may not be null")
    private List<Long> epicIds;

    @NotNull(message = "List of userStories may not be null")
    private List<Long> userStoryIds;

    private Long projectId;

    @NotNull(message = "List of sprintBacklog Ids may not be null")
    private List<Long> sprintBacklogIds;


}
