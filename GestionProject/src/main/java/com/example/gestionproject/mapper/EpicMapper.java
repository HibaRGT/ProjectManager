package com.example.gestionproject.mapper;

import com.example.gestionproject.dto.EpicDTO;
import com.example.gestionproject.model.Epic;
import com.example.gestionproject.model.ProductBacklog;
import com.example.gestionproject.model.UserStory;
import java.util.List;

public class EpicMapper {

    private EpicMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static EpicDTO toDTO(Epic epic) {
        if (epic == null) {
            return null;
        }

        return EpicDTO.builder()
                .id(epic.getId())
                .nom(epic.getNom())
                .description(epic.getDescription())
                .productBacklogId(extractProductBacklogId(epic))
                .userStoryIds(extractUserStoryIds(epic))
                .build();
    }

    public static Epic toEntity(EpicDTO dto, ProductBacklog backlog) {
        return Epic.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .description(dto.getDescription())
                .productBacklog(backlog)
                .build();
    }

    private static Long extractProductBacklogId(Epic epic) {
        return epic.getProductBacklog() == null
                ? null
                : epic.getProductBacklog().getId();
    }

    private static List<Long> extractUserStoryIds(Epic epic) {
        return epic.getUserStories() == null
                ? List.of()
                : epic.getUserStories()
                .stream()
                .map(UserStory::getId)
                .toList();
    }
}
