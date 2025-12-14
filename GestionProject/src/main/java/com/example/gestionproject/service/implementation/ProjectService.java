package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.ProjectDTO;
import com.example.gestionproject.exception.ProjectNotFoundException;
import com.example.gestionproject.model.Project;
import com.example.gestionproject.repository.ProjectRepository;
import com.example.gestionproject.service.interfaces.ProjectInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProjectService implements ProjectInterface {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository pr){
        this.projectRepository = pr;
    }

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        validateProject(projectDTO);

        Project project = Project.builder()
                .nom(projectDTO.getNom())
                .description(projectDTO.getDescription())
                .build();


        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }

    @Override
    public List<ProjectDTO> getAllProject() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        validateProjectId(id);

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Aucun projet trouvé avec l'ID: " + id));

        return convertToDTO(project);
    }

    @Override
    public void deleteProject(Long id) {
        validateProjectId(id);
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException("Project non trouvé avec l'ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        validateProjectId(id);
        Project pr = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project non trouvé avec l'ID: " + id));

        validateProject(projectDTO);

        pr.setNom(projectDTO.getNom());
        pr.setDescription(projectDTO.getDescription());

        Project updatedProject = projectRepository.save(pr);
        return convertToDTO(updatedProject);
    }


    private void validateProject(ProjectDTO projectDTO) {
        if (projectDTO.getNom() == null || projectDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le titre du projet ne peut pas être vide");
        }
        if (projectDTO.getDescription() == null || projectDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description du projet ne peut pas être vide");
        }
    }

    private void validateProjectId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du projet ne peut pas être null");
        }
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = ProjectDTO.builder()
                .id(project.getId())
                .nom(project.getNom())
                .description(project.getDescription())
                .build();

        if (project.getProductBacklog() != null) {
            dto.setProductBacklogId(project.getProductBacklog().getId());
        }

        return dto;
    }
}