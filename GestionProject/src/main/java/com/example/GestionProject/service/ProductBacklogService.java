package com.example.GestionProject.service;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.repository.ProductBacklogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductBacklogService {
    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public ProductBacklogService(ProductBacklogRepository productBacklogRepository) {
        this.productBacklogRepository = productBacklogRepository;
    }

    public ProductBacklog createProductBacklog(ProductBacklog backlog) {
        if (backlog.getNom() == null || backlog.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du backlog ne peut pas être vide");
        }
        if (backlog.getDescription() == null || backlog.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du backlog ne peut pas être vide");
        }
        return productBacklogRepository.save(backlog);
    }

    public ProductBacklog getProductBacklogById(Long id) {
        return productBacklogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + id));
    }

    public List<ProductBacklog> getAllProductBacklogs() {
        return productBacklogRepository.findAll();
    }

    public void deleteProductBacklog(Long id) {
        productBacklogRepository.deleteById(id);
    }

    public ProductBacklog updateProductBacklog(Long id, ProductBacklog backlog) {
        ProductBacklog existingBacklog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + id));

        if (backlog.getNom() == null || backlog.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du backlog ne peut pas être vide");
        }
        if (backlog.getDescription() == null || backlog.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du backlog ne peut pas être vide");
        }

        existingBacklog.setNom(backlog.getNom());
        existingBacklog.setDescription(backlog.getDescription());

        return productBacklogRepository.save(existingBacklog);
    }
}
