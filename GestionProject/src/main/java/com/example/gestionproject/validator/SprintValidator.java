package com.example.gestionproject.validator;

import com.example.gestionproject.dto.SprintDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class SprintValidator {
    public void validateSprintData(SprintDTO sprint) {
        if (sprint.getName() == null || sprint.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom du sprint ne peut pas être vide");
        }

        if (sprint.getStartDate() == null) {
            throw new IllegalArgumentException("La date de début du sprint est obligatoire");
        }

        if (sprint.getEndDate() == null) {
            throw new IllegalArgumentException("La date de fin du sprint est obligatoire");
        }

        if (sprint.getEndDate().isBefore(sprint.getStartDate())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }

        LocalDate start = sprint.getStartDate();
        LocalDate end = sprint.getEndDate();
        long daysBetween = ChronoUnit.DAYS.between(start, end);

        if (daysBetween < 7) {
            throw new IllegalArgumentException("La durée du sprint ne peut pas être inférieure à 7 jours");
        }

        if (daysBetween > 30) {
            throw new IllegalArgumentException("La durée du sprint ne peut pas dépasser 30 jours");
        }
    }

    public void validateSprintId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du sprint ne peut pas être null");
        }
    }
}
