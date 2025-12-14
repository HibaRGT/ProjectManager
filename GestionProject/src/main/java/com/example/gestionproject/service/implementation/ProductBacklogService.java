package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.ProductBacklogDTO;
import com.example.gestionproject.exception.ProductBacklogNotFound;
import com.example.gestionproject.exception.ProjectNotFoundException;
import com.example.gestionproject.mapper.ProductBacklogMapper;
import com.example.gestionproject.model.*;
import com.example.gestionproject.repository.ProductBacklogRepository;
import com.example.gestionproject.repository.ProjectRepository;
import com.example.gestionproject.service.interfaces.ProductBacklogInterface;
import com.example.gestionproject.validator.ProductBacklogValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductBacklogService implements ProductBacklogInterface {
    private final ProductBacklogRepository productBacklogRepository;
    private final ProjectRepository projectRepository;
    private final ProductBacklogValidator validator;
    private static final String NOT_FOUND_PROJECT = "Projet non trouvé avec l'ID: ";

    @Autowired
    public ProductBacklogService(ProductBacklogRepository productBacklogRepository, ProjectRepository projectRepository, ProductBacklogValidator validator) {
        this.productBacklogRepository = productBacklogRepository;
        this.projectRepository = projectRepository;
        this.validator = validator;
    }

    @Override
    public ProductBacklogDTO createProductBacklog(ProductBacklogDTO backlog) {
        validator.validateProductBacklog(backlog);

        ProductBacklog pb = ProductBacklog.builder()
                .nom(backlog.getNom())
                .description(backlog.getDescription())
                .build();

        validator.validateProjectAssociation(backlog.getProjectId());
        Project project = getProjectOrThrow(backlog.getProjectId());
        pb.setProject(project);
        project.setProductBacklog(pb);

        return ProductBacklogMapper.toDTO(productBacklogRepository.save(pb));
    }

    @Override
    public ProductBacklogDTO getProductBacklogById(Long id) {
        validator.validateProductBacklogId(id);

        return ProductBacklogMapper.toDTO(
                getBacklogOrThrow(id)
        );
    }

    @Override
    public ProductBacklogDTO getProductBacklogByProjectId(Long id) {
        validator.validateProjectId(id);
        Project project = getProjectOrThrow(id);
        ProductBacklog pb = project.getProductBacklog();
        return ProductBacklogMapper.toDTO(pb);
    }

    @Override
    public List<ProductBacklogDTO> getAllProductBacklogs() {
        return productBacklogRepository.findAll()
                .stream()
                .map(ProductBacklogMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteProductBacklog(Long id) {
        validator.validateProductBacklogId(id);
        productBacklogRepository.delete(
                getBacklogOrThrow(id)
        );
    }

    @Override
    public ProductBacklogDTO updateProductBacklog(Long id, ProductBacklogDTO backlogDTO) {
        validator.validateProductBacklogId(id);
        validator.validateProductBacklog(backlogDTO);

        ProductBacklog existingBacklog = getBacklogOrThrow(id);

        existingBacklog.setNom(backlogDTO.getNom());
        existingBacklog.setDescription(backlogDTO.getDescription());

        if (backlogDTO.getProjectId() != null) {
            existingBacklog.setProject(
                    getProjectOrThrow(backlogDTO.getProjectId())
            );
        }

        return ProductBacklogMapper.toDTO(
                productBacklogRepository.save(existingBacklog)
        );
    }

    /* ===== Private helpers ===== */

    private ProductBacklog getBacklogOrThrow(Long id) {
        return productBacklogRepository.findById(id)
                .orElseThrow(() ->
                        new ProductBacklogNotFound("ProductBacklog non trouvé avec l'ID: " + id));
    }

    private Project getProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(NOT_FOUND_PROJECT + projectId));
    }
}
