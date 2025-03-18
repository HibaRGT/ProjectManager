package com.example.GestionProject.service.implementation;

import com.example.GestionProject.dto.ProductBacklogDTO;
import com.example.GestionProject.dto.ProjectDTO;
import com.example.GestionProject.model.*;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.ProjectRepository;
import com.example.GestionProject.service.interfaces.ProductBacklogInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductBacklogService implements ProductBacklogInterface {
    private final ProductBacklogRepository productBacklogRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProductBacklogService(ProductBacklogRepository productBacklogRepository, ProjectRepository projectRepository) {
        this.productBacklogRepository = productBacklogRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ProductBacklogDTO createProductBacklog(ProductBacklogDTO backlog) {
        validateProductBacklog(backlog);

        ProductBacklog pb = new ProductBacklog();
        pb.setNom(backlog.getNom());
        pb.setDescription(backlog.getDescription());

        if (backlog.getProjectId() != null) {
            Project project = projectRepository.findById(backlog.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + backlog.getProjectId()));
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
        ProductBacklog backlog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + id));
        return convertToDTO(backlog);
    }

    @Override
    public ProductBacklogDTO getProductBacklogByProjectId(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + id));
        ProductBacklog pb = project.getProductBacklog();
        return convertToDTO(pb);
    }

    @Override
    public List<ProductBacklogDTO> getAllProductBacklogs() {
        List<ProductBacklog> backlogs = productBacklogRepository.findAll();
        return backlogs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductBacklog(Long id) {
        if (!productBacklogRepository.existsById(id)) {
            throw new RuntimeException("ProductBacklog non trouvée avec l'ID: " + id);
        }
        productBacklogRepository.deleteById(id);
    }

    @Override
    public ProductBacklogDTO updateProductBacklog(Long id, ProductBacklogDTO backlogDTO) {
        ProductBacklog existingBacklog = productBacklogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + id));

        validateProductBacklog(backlogDTO);

        existingBacklog.setNom(backlogDTO.getNom());
        existingBacklog.setDescription(backlogDTO.getDescription());

            if (backlogDTO.getProjectId() != null) {
            Project project = projectRepository.findById(backlogDTO.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + backlogDTO.getProjectId()));
            existingBacklog.setProject(project);
        }

        ProductBacklog updatedBacklog = productBacklogRepository.save(existingBacklog);
        return convertToDTO(updatedBacklog);
    }

    private void validateProductBacklog(ProductBacklogDTO backlog) {
        if (backlog.getNom() == null || backlog.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du backlog ne peut pas être vide");
        }
        if (backlog.getDescription() == null || backlog.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du backlog ne peut pas être vide");
        }
    }

    public ProductBacklogDTO convertToDTO(ProductBacklog backlog) {
        return new ProductBacklogDTO(
                backlog.getId(),
                backlog.getNom(),
                backlog.getDescription(),
                backlog.getEpics() != null ?
                        backlog.getEpics().stream().map(Epic::getId).collect(Collectors.toList())
                        : new ArrayList<Long>(),
                backlog.getUserStories()!= null ?
                        backlog.getUserStories().stream().map(UserStory::getId).collect(Collectors.toList())
                        : new ArrayList<Long>(),
                backlog.getProject() != null ? backlog.getProject().getId() : null,
                backlog.getSprintBacklogs() != null
                        ? backlog.getSprintBacklogs().stream().map(SprintBacklog::getId).collect(Collectors.toList())
                        : new ArrayList<Long>()
                );
    }

    /// //////////////////////////////////////////////
//    public ProductBacklog convertToEntity(ProductBacklogDTO dto) {
//        ProductBacklog entity = new ProductBacklog();
//        entity.setId(dto.getId());
//        entity.setNom(dto.getNom());
//        entity.setDescription(dto.getDescription());
//
//        if (dto.getProjectId() != null) {
//            Project project = projectRepository.findById(dto.getProjectId())
//                    .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + dto.getProjectId()));
//            entity.setProject(project);
//        }
//
//        return entity;
//    }

}
