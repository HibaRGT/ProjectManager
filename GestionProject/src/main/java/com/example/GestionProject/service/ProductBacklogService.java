package com.example.GestionProject.service;

import com.example.GestionProject.repository.ProductBacklogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductBacklogService {
    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public ProductBacklogService(ProductBacklogRepository productBacklogRepository) {
        this.productBacklogRepository = productBacklogRepository;
    }
}
