package com.example.gestionproject.validator;

import com.example.gestionproject.dto.SprintBacklogDTO;
import org.springframework.stereotype.Component;

@Component
public class SprintBacklogValidator {

    public void validateSprintBacklogId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du sprint backlog ne peut pas etre null");
        }
    }

    public void validateSprintId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du sprint ne peut pas etre null");
        }
    }

    public void validateCreate(SprintBacklogDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("SprintBacklogDTO ne peut pas être null");
        }
        if (dto.getSprintId() == null) {
            throw new IllegalArgumentException("Le sprintBacklog n'est lié à aucun sprint");
        }
        if (dto.getProductBacklogId() == null) {
            throw new IllegalArgumentException("Le sprintBacklog n'est lié à aucun ProductBacklog");
        }
    }

}
