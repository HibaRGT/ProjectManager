package com.example.GestionProject.service;

import com.example.GestionProject.model.Epic;
import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.Project;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public ProjectService(ProjectRepository pr, ProductBacklogRepository pbr){
        this.projectRepository = pr;
        this.productBacklogRepository = pbr;
    }

    public Project createProject(Project project){
        validateProject(project);
        return projectRepository.save(project);
    }

    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project introuvable avec l'ID: " + id);
        }
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucun projet trouvée avec l'ID: " + id));
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project non trouvé avec l'ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    public Project updateProject(Long id, Project project) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project non trouvé avec l'ID: " + id));

        if (project.getNom() == null || project.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom du project ne peut pas être vide");
        }
        if (project.getDescription() == null || project.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du project ne peut pas être vide");
        }

        existingProject.setNom(project.getNom());
        existingProject.setDescription(project.getDescription());

        return projectRepository.save(existingProject);
    }

    @Transactional
    public Project addProductBacklogToProject(Long projectId, ProductBacklog productBacklog) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project introuvable avec ID: " + projectId));

        if (productBacklog.getId() == null || !productBacklogRepository.existsById(productBacklog.getId())) {
            productBacklog = productBacklogRepository.save(productBacklog);
        }

        project.setProductBacklog(productBacklog);
        return projectRepository.save(project);
    }


    private void validateProject(Project project) {
        if (project.getNom() == null || project.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le titre du projet ne peut pas être vide");
        }
        if (project.getDescription() == null || project.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du project ne peut pas être vide");
        }
    }
}
