package com.example.gestionproject.validator;

import com.example.gestionproject.dto.ProjectDTO;
import org.springframework.stereotype.Component;

@Component
public class ProjectValidator {
    public void validateProject(ProjectDTO projectDTO) {
        if (projectDTO.getNom() == null || projectDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le titre du projet ne peut pas être vide");
        }
        if (projectDTO.getDescription() == null || projectDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du projet ne peut pas être vide");
        }
    }

    public void validateProjectId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du projet ne peut pas être null");
        }
    }
}
