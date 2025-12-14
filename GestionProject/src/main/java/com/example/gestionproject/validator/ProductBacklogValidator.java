package com.example.gestionproject.validator;

import com.example.gestionproject.dto.ProductBacklogDTO;
import com.example.gestionproject.exception.ProductBacklogNotAssociatedWithProjectException;
import org.springframework.stereotype.Component;

@Component
public class ProductBacklogValidator {

    public void validateProductBacklog(ProductBacklogDTO backlog) {
        if (backlog.getNom() == null || backlog.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du backlog ne peut pas être vide");
        }
        if (backlog.getDescription() == null || backlog.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du backlog ne peut pas être vide");
        }
    }

    public void validateProductBacklogId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du produit backlog ne peut pas être null");
        }
    }

    public void validateProjectId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du projet ne peut pas être null");
        }
    }

    public void validateProjectAssociation(Long projectId) {
        if (projectId == null) {
            throw new ProductBacklogNotAssociatedWithProjectException(
                    "ProductBacklog doit être associé à un projet"
            );
        }
    }
}
