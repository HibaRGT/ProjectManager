package com.example.gestionproject.validator;

import com.example.gestionproject.dto.UserStoryDTO;
import com.example.gestionproject.model.SprintBacklog;
import com.example.gestionproject.model.UserStory;
import org.springframework.stereotype.Component;

@Component
public class UserStoryValidator {

    public void validateUserStoryDTO(UserStoryDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("UserStoryDTO cannot be null");
        }
        validateTitle(dto.getTitre());
        validateDescription(dto.getDescription());
    }

    public void validateTitle(String titre) {
        if (titre == null || titre.isEmpty()) {
            throw new IllegalArgumentException("Le titre de la user story ne peut pas être vide");
        }
    }

    public void validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("La description de la user story ne peut pas être vide");
        }
    }

    public void validateBacklogId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du backlog ne peut pas être null");
        }
    }

    public void validateUserStoryId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du userStory ne peut pas être null");
        }
    }

    public void validateSprintBacklogId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du sprint backlog ne peut pas être null");
        }
    }

    public void validateEpicId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de l'Epic ne peut pas être null");
        }
    }

    public void validateTaskId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du Task ne peut pas être null");
        }
    }

    public void validateUserStory(UserStoryDTO userStoryDTO) {
        if (userStoryDTO.getTitre() == null || userStoryDTO.getTitre().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la user story ne peut pas être vide");
        }
        if (userStoryDTO.getDescription() == null || userStoryDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de la user story ne peut pas être vide");
        }
    }

    public void validateUserStoryNotAssigned(UserStory userStory) {
        if (userStory.getSprintBacklog() != null) {
            throw new IllegalArgumentException("Cette User Story est déjà assignée à un Sprint");
        }
    }

    public void validateUserStoryAssignedToSprint(UserStory userStory, SprintBacklog sprintBacklog) {
        if (userStory.getSprintBacklog() == null
                || !userStory.getSprintBacklog().getId().equals(sprintBacklog.getId())) {
            throw new IllegalStateException("Cette User Story n'est pas assignée à ce Sprint");
        }
    }
}