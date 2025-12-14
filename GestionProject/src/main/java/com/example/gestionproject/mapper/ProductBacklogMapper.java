package com.example.gestionproject.mapper;

import com.example.gestionproject.dto.ProductBacklogDTO;
import com.example.gestionproject.model.Epic;
import com.example.gestionproject.model.ProductBacklog;
import com.example.gestionproject.model.SprintBacklog;
import com.example.gestionproject.model.UserStory;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ProductBacklogMapper {

    private ProductBacklogMapper(){
        throw new IllegalStateException("Utility class");
    }

    public static ProductBacklogDTO toDTO(ProductBacklog backlog) {
        return ProductBacklogDTO.builder()
                .id(backlog.getId())
                .nom(backlog.getNom())
                .description(backlog.getDescription())
                .epicIds(backlog.getEpics() != null ?
                        backlog.getEpics().stream().map(Epic::getId).collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>())
                .userStoryIds( backlog.getUserStories() != null ?
                        backlog.getUserStories().stream().map(UserStory::getId).collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>())
                .projectId(backlog.getProject() != null ? backlog.getProject().getId() : null)
                .sprintBacklogIds(backlog.getSprintBacklogs() != null
                        ? backlog.getSprintBacklogs().stream().map(SprintBacklog::getId).collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>())
                .build();
    }
}
