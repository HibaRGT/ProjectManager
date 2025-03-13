package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.model.ProductBacklog;

import java.util.List;

public interface ProductBacklogInterface {
    public ProductBacklog createProductBacklog(ProductBacklog backlog);
    public ProductBacklog getProductBacklogById(Long id);
    public List<ProductBacklog> getAllProductBacklogs();
    public void deleteProductBacklog(Long id);
    public ProductBacklog updateProductBacklog(Long id, ProductBacklog backlog);

}

