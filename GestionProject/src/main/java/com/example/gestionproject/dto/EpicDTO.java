package com.example.gestionproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EpicDTO implements Serializable {

    @NotNull(message = "id may not be null")
    private Long id;

    @NotBlank(message = "nom may not be blank")
    private String nom;

    @NotBlank(message = "description may not be blank")
    private String description;

    private Long productBacklogId;

    @NotNull(message = "List of userStory Ids may not be null")
    @Builder.Default
    private List<Long> userStoryIds = new ArrayList<>();

}
