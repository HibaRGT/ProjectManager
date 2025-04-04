package com.example.GestionProject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SprintBacklogDTO implements Serializable {

    @NotNull(message = "id may not be null")
    private Long id;

    @NotNull(message = "sprint Id may not be null")
    private Long sprintId;

    @NotNull(message = "List of userStories may not be null")
    @Builder.Default
    private List<Long> userStoryIds = new ArrayList<>();

    private Long productBacklogId;
}
