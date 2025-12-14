package com.example.gestionproject.validator;

import com.example.gestionproject.dto.TaskDTO;
import com.example.gestionproject.model.UserStory;
import org.springframework.stereotype.Component;

@Component
public class TaskValidator {

    public void validateTaskData(TaskDTO taskDTO) {
        if (taskDTO.getTitle() == null || taskDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la tâche ne peut pas être vide");
        }

        if (taskDTO.getDescription() == null || taskDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de la tâche ne peut pas être vide");
        }

        if (taskDTO.getStatus() == null) {
            throw new IllegalArgumentException("Le status de la tâche ne peut pas être vide");
        }
    }

    public void validateTaskId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du task ne peut pas etre null");
        }
    }

    public void validateUserStoryId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du userStory ne peut pas etre null");
        }
    }

    public void validateUserStoryAssignedToSprint(UserStory userStory) {
        if (userStory.getSprintBacklog() == null) {
            throw new IllegalStateException(
                    "Impossible d'ajouter des tâches à une User Story qui n'est pas dans un Sprint"
            );
        }
    }
}
