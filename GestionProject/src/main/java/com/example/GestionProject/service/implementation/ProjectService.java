package com.example.GestionProject.service.implementation;

import com.example.GestionProject.dto.ProductBacklogDTO;
import com.example.GestionProject.dto.ProjectDTO;
import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.Project;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.ProjectRepository;
import com.example.GestionProject.service.interfaces.ProjectInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService implements ProjectInterface {

    private final ProjectRepository projectRepository;
    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public ProjectService(ProjectRepository pr, ProductBacklogRepository pbr){
        this.projectRepository = pr;
        this.productBacklogRepository = pbr;
    }

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        validateProject(projectDTO);

        Project project = new Project();
        project.setNom(projectDTO.getNom());
        project.setDescription(projectDTO.getDescription());

        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    @Override
    public List<ProjectDTO> getAllProject() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project introuvable avec l'ID: " + id);
        }
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucun projet trouvé avec l'ID: " + id));
        return convertToDTO(project);
    }

    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project non trouvé avec l'ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project pr = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project non trouvé avec l'ID: " + id));

        validateProject(projectDTO);

        pr.setNom(projectDTO.getNom());
        pr.setDescription(projectDTO.getDescription());

        Project updatedProject = projectRepository.save(pr);
        return convertToDTO(updatedProject);
    }
//
//    @Override
//    public Project addProductBacklogToProject(Long projectId, ProductBacklog productBacklog) {
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new EntityNotFoundException("Project introuvable avec ID: " + projectId));
//
//        if (productBacklog.getId() == null || !productBacklogRepository.existsById(productBacklog.getId())) {
//            productBacklog = productBacklogRepository.save(productBacklog);
//        }
//
//        project.setProductBacklog(productBacklog);
//        return projectRepository.save(project);
//    }


    private void validateProject(ProjectDTO projectDTO) {
        if (projectDTO.getNom() == null || projectDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le titre du projet ne peut pas être vide");
        }
        if (projectDTO.getDescription() == null || projectDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du project ne peut pas être vide");
        }
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setNom(project.getNom());
        dto.setDescription(project.getDescription());

        if (project.getProductBacklog() != null) {
            dto.setProductBacklogId(project.getProductBacklog().getId());
        }

        return dto;
    }
}
