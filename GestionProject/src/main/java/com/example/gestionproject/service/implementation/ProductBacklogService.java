package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.ProductBacklogDTO;
import com.example.gestionproject.model.*;
import com.example.gestionproject.repository.ProductBacklogRepository;
import com.example.gestionproject.repository.ProjectRepository;
import com.example.gestionproject.service.interfaces.ProductBacklogInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductBacklogService implements ProductBacklogInterface {
    private final ProductBacklogRepository productBacklogRepository;
    private final ProjectRepository projectRepository;
    private static final String NOT_FOUND_PROJECT = "Projet non trouvé avec l'ID: ";

    @Autowired
    public ProductBacklogService(ProductBacklogRepository productBacklogRepository, ProjectRepository projectRepository) {
        this.productBacklogRepository = productBacklogRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ProductBacklogDTO createProductBacklog(ProductBacklogDTO backlog) {
        validateProductBacklog(backlog);

        ProductBacklog pb = ProductBacklog.builder()
                .nom(backlog.getNom())
                .description(backlog.getDescription())
                .build();

        if (backlog.getProjectId() != null) {
            Project project = projectRepository.findById(backlog.getProjectId())
                    .orElseThrow(() -> new RuntimeException(NOT_FOUND_PROJECT + backlog.getProjectId()));
            pb.setProject(project);
            project.setProductBacklog(pb);
        }else{
            throw new RuntimeException("ProductBacklog doit etre associé a un projet");
        }

        ProductBacklog res = productBacklogRepository.save(pb);
        return convertToDTO(res);
    }

    @Override
    public ProductBacklogDTO getProductBacklogById(Long id) {
        validateProductBacklogId(id);

        ProductBacklog backlog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + id));
        return convertToDTO(backlog);
    }

    @Override
    public ProductBacklogDTO getProductBacklogByProjectId(Long id) {
        if(id == null){
            throw new IllegalArgumentException("L'ID du projet ne peut pas être null");
        }
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_PROJECT + id));
        ProductBacklog pb = project.getProductBacklog();
        return convertToDTO(pb);
    }

    @Override
    public List<ProductBacklogDTO> getAllProductBacklogs() {
        List<ProductBacklog> backlogs = productBacklogRepository.findAll();
        return backlogs.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public void deleteProductBacklog(Long id) {
        validateProductBacklogId(id);

        if (!productBacklogRepository.existsById(id)) {
            throw new RuntimeException("ProductBacklog non trouvée avec l'ID: " + id);
        }
        productBacklogRepository.deleteById(id);
    }

    @Override
    public ProductBacklogDTO updateProductBacklog(Long id, ProductBacklogDTO backlogDTO) {
        validateProductBacklogId(id);

        ProductBacklog existingBacklog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + id));

        validateProductBacklog(backlogDTO);

        existingBacklog.setNom(backlogDTO.getNom());
        existingBacklog.setDescription(backlogDTO.getDescription());

        if (backlogDTO.getProjectId() != null) {
            Project project = projectRepository.findById(backlogDTO.getProjectId())
                    .orElseThrow(() -> new RuntimeException(NOT_FOUND_PROJECT + backlogDTO.getProjectId()));
            existingBacklog.setProject(project);
        }

        ProductBacklog updatedBacklog = productBacklogRepository.save(existingBacklog);
        return convertToDTO(updatedBacklog);
    }

    //------------------------------------------------------------------------------------------------------------------

    private void validateProductBacklog(ProductBacklogDTO backlog) {
        if (backlog.getNom() == null || backlog.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du backlog ne peut pas être vide");
        }
        if (backlog.getDescription() == null || backlog.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du backlog ne peut pas être vide");
        }
    }

    private void validateProductBacklogId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du produit backlog ne peut pas être null");
        }
    }


    public ProductBacklogDTO convertToDTO(ProductBacklog backlog) {
        return ProductBacklogDTO.builder()
                .id(backlog.getId())
                .nom(backlog.getNom())
                .description(backlog.getDescription())
                .epicIds(backlog.getEpics() != null ?
                        backlog.getEpics().stream().map(Epic::getId).collect(Collectors.toList())
                        : new ArrayList<>())
                .userStoryIds( backlog.getUserStories() != null ?
                        backlog.getUserStories().stream().map(UserStory::getId).collect(Collectors.toList())
                        : new ArrayList<>())
                .projectId(backlog.getProject() != null ? backlog.getProject().getId() : null)
                .sprintBacklogIds(backlog.getSprintBacklogs() != null
                        ? backlog.getSprintBacklogs().stream().map(SprintBacklog::getId).collect(Collectors.toList())
                        : new ArrayList<>())

                .build();
    }


}
