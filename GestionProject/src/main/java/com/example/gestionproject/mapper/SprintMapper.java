package com.example.gestionproject.mapper;

import com.example.gestionproject.dto.SprintDTO;
import com.example.gestionproject.model.Sprint;

public class SprintMapper {
    private SprintMapper() {
        throw new IllegalStateException("Utility class");
    }
    public static SprintDTO toDTO(Sprint sprint) {
        SprintDTO dto = SprintDTO.builder()
                .id(sprint.getId())
                .name(sprint.getName())
                .description(sprint.getDescription())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .build();

        if (sprint.getSprintBacklog() != null) {
            dto.setSprintBacklogId(sprint.getSprintBacklog().getId());
        }

        return dto;
    }

    public static Sprint toEntity(SprintDTO dto) {
        if (dto == null) return null;
        return Sprint.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }
}
