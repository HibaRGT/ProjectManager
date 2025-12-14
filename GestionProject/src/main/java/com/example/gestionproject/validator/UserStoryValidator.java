package com.example.gestionproject.validator;

import com.example.gestionproject.dto.UserStoryDTO;
import com.example.gestionproject.model.SprintBacklog;
import com.example.gestionproject.model.UserStory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserStoryValidator {

    private void requireNonNull(Object value, String message) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException(message);
        }
    }

    public void validateBacklogId(Long id) {
        requireNonNull(id, "L'ID du backlog ne peut pas être null");
    }

    public void validateUserStoryId(Long id) {
        requireNonNull(id, "L'ID du userStory ne peut pas être null");
    }

    public void validateSprintBacklogId(Long id) {
        requireNonNull(id, "L'ID du sprint backlog ne peut pas être null");
    }

    public void validateEpicId(Long id) {
        requireNonNull(id, "L'ID de l'Epic ne peut pas être null");
    }

    public void validateTaskId(Long id) {
        requireNonNull(id, "L'ID du Task ne peut pas être null");
    }

    private void requireNonEmpty(String value, String message) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public void validateUserStory(UserStoryDTO userStoryDTO) {
        requireNonEmpty(userStoryDTO.getTitre(), "Le titre de la user story ne peut pas être vide");
        requireNonEmpty(userStoryDTO.getDescription(), "La description de la user story ne peut pas être vide");
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