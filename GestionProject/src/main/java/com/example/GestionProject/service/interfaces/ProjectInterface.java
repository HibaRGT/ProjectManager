package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.dto.ProductBacklogDTO;
import com.example.GestionProject.dto.ProjectDTO;
import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.Project;

import java.util.List;

public interface ProjectInterface {
    public ProjectDTO createProject(ProjectDTO project);
    public List<ProjectDTO> getAllProject();
    public ProjectDTO getProjectById(Long id);
    public void deleteProject(Long id);
    public ProjectDTO updateProject(Long id, ProjectDTO project);
}
