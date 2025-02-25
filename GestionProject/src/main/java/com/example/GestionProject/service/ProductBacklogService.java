package com.example.GestionProject.service;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.repository.ProductBacklogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductBacklogService {

    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public ProductBacklogService(ProductBacklogRepository productBacklogRepository) {
        this.productBacklogRepository = productBacklogRepository;
    }

    public List<ProductBacklog> getAllProductBacklogs() {
        return productBacklogRepository.findAll();
    }

    public Optional<ProductBacklog> getProductBacklogById(Long id) {
        return productBacklogRepository.findById(id);
    }

    public ProductBacklog saveProductBacklog(ProductBacklog productBacklog) {
        return productBacklogRepository.save(productBacklog);
    }

    public void deleteProductBacklog(Long id) {
        productBacklogRepository.deleteById(id);
    }

}
