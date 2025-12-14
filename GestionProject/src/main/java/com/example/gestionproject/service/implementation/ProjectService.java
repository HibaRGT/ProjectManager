package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.ProjectDTO;
import com.example.gestionproject.exception.ProjectNotFoundException;
import com.example.gestionproject.mapper.ProjectMapper;
import com.example.gestionproject.model.Project;
import com.example.gestionproject.repository.ProjectRepository;
import com.example.gestionproject.service.interfaces.ProjectInterface;
import com.example.gestionproject.validator.ProjectValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProjectService implements ProjectInterface {

    private final ProjectRepository projectRepository;
    private final ProjectValidator projectValidator;

    @Autowired
    public ProjectService(ProjectRepository pr, ProjectValidator projectValidator){
        this.projectRepository = pr;
        this.projectValidator = projectValidator;
    }

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        projectValidator.validateProject(projectDTO);

        Project project = Project.builder()
                .nom(projectDTO.getNom())
                .description(projectDTO.getDescription())
                .build();


        Project savedProject = projectRepository.save(project);
        return ProjectMapper.toDTO(savedProject);
    }

    @Override
    public List<ProjectDTO> getAllProject() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        projectValidator.validateProjectId(id);

        Project project = getProjectOrThrow(id);
        return ProjectMapper.toDTO(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectValidator.validateProjectId(id);
        Project project = getProjectOrThrow(id);
        projectRepository.delete(project);
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        projectValidator.validateProjectId(id);

        Project pr = getProjectOrThrow(id);
        projectValidator.validateProject(projectDTO);

        pr.setNom(projectDTO.getNom());
        pr.setDescription(projectDTO.getDescription());

        Project updatedProject = projectRepository.save(pr);
        return ProjectMapper.toDTO(updatedProject);
    }

    private Project getProjectOrThrow(Long id) {
        projectValidator.validateProjectId(id);
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project non trouvé avec l'ID: " + id));
    }


}