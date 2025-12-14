package com.example.gestionproject.validator;

import com.example.gestionproject.dto.EpicDTO;
import org.springframework.stereotype.Component;

@Component
public class EpicValidator {
    public void validate(EpicDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("EpicDTO ne peut pas être null");
        }

        if (dto.getNom() == null || dto.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'epic ne peut pas être vide");
        }

        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("La description de l'epic ne peut pas être vide");
        }
    }

    public void validateId(Long id, String entityName) {
        if (id == null) {
            throw new IllegalArgumentException(entityName + " ID ne peut pas être null");
        }
    }
}
