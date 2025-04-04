package com.example.GestionProject.dto;

import com.example.GestionProject.model.SprintBacklog;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SprintDTO implements Serializable {

    @NotNull(message = "id may not be null")
    private Long id;

    @NotBlank(message = "name may not be blank")
    private String name;

    @NotNull(message = "description may not be null")
    private String description;

    @NotNull(message = "startDate may not be null")
    private LocalDate startDate;

    @NotNull(message = "endDate may not be null")
    private LocalDate endDate;

    private Long sprintBacklogId;

}
