package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.dto.ProductBacklogDTO;
import com.example.GestionProject.model.ProductBacklog;

import java.util.List;

public interface ProductBacklogInterface {
    public ProductBacklogDTO createProductBacklog(ProductBacklogDTO backlog);
    public ProductBacklogDTO getProductBacklogById(Long id);
    public ProductBacklogDTO getProductBacklogByProjectId(Long id);
    public List<ProductBacklogDTO> getAllProductBacklogs();
    public void deleteProductBacklog(Long id);
    public ProductBacklogDTO updateProductBacklog(Long id, ProductBacklogDTO backlog);
}

