package com.example.GestionProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SprintBacklogDTO implements Serializable {

    @NotNull(message = "id may not be null")
    private Long id;

    @NotNull(message = "sprint Id may not be null")
    private Long sprintId;

    @NotNull(message = "List of userStories may not be null")
    private List<Long> userStoryIds;

    private Long productBacklogId;
}
