package com.example.gestionproject.service.interfaces;

import com.example.gestionproject.dto.ProjectDTO;

import java.util.List;

public interface ProjectInterface {
    public ProjectDTO createProject(ProjectDTO project);
    public List<ProjectDTO> getAllProject();
    public ProjectDTO getProjectById(Long id);
    public void deleteProject(Long id);
    public ProjectDTO updateProject(Long id, ProjectDTO project);
}
