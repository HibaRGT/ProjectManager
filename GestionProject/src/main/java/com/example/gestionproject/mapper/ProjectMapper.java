package com.example.gestionproject.mapper;

import com.example.gestionproject.dto.ProjectDTO;
import com.example.gestionproject.model.Project;

public class ProjectMapper {

    private ProjectMapper(){
        throw new IllegalStateException("Utility class");
    }

    public static ProjectDTO toDTO(Project project) {
        ProjectDTO dto = ProjectDTO.builder()
                .id(project.getId())
                .nom(project.getNom())
                .description(project.getDescription())
                .build();

        if (project.getProductBacklog() != null) {
            dto.setProductBacklogId(project.getProductBacklog().getId());
        }

        return dto;
    }
}
