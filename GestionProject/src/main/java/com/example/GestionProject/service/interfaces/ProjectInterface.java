package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.Project;

import java.util.List;

public interface ProjectInterface {
    public Project createProject(Project project);
    public List<Project> getAllProject();
    public Project getProjectById(Long id);
    public void deleteProject(Long id);
    public Project updateProject(Long id, Project project);
    public Project addProductBacklogToProject(Long projectId, ProductBacklog productBacklog);
}
