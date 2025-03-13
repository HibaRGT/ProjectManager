package com.example.GestionProject.service.implementation;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.service.interfaces.ProductBacklogInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductBacklogService implements ProductBacklogInterface {
    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public ProductBacklogService(ProductBacklogRepository productBacklogRepository) {
        this.productBacklogRepository = productBacklogRepository;
    }

    @Override
    public ProductBacklog createProductBacklog(ProductBacklog backlog) {
        if (backlog.getNom() == null || backlog.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du backlog ne peut pas être vide");
        }
        if (backlog.getDescription() == null || backlog.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du backlog ne peut pas être vide");
        }
        return productBacklogRepository.save(backlog);
    }

    @Override
    public ProductBacklog getProductBacklogById(Long id) {
        return productBacklogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + id));
    }

    @Override
    public List<ProductBacklog> getAllProductBacklogs() {
        return productBacklogRepository.findAll();
    }

    @Override
    public void deleteProductBacklog(Long id) {
        if (!productBacklogRepository.existsById(id)) {
            throw new RuntimeException("ProductBacklog non trouvée avec l'ID: " + id);
        }
        productBacklogRepository.deleteById(id);
    }

    @Override
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
