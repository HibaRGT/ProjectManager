package com.example.gestionproject.dto;

import com.example.gestionproject.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO implements Serializable {

    @NotNull(message = "id may not be null")
    private Long id;

    @NotBlank(message = "title may not be blank")
    private String title;

    @NotBlank(message = "Description may not be blank")
    private String description;

    @NotNull(message = "status may not be null")
    private TaskStatus status;

    @NotNull(message = "The userStory Id may not be null")
    private Long userStoryId;

}
