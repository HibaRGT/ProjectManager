package com.example.GestionProject.controller;

import com.example.GestionProject.dto.ProductBacklogDTO;
import com.example.GestionProject.dto.ProjectDTO;
import com.example.GestionProject.model.Project;
import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.service.implementation.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDto) {
        ProjectDTO createdProjectDTO = projectService.createProject(projectDto);
        return new ResponseEntity<>(createdProjectDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO projectDto = projectService.getProjectById(id);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProject();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDto) {
        ProjectDTO updatedProject = projectService.updateProject(id, projectDto);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }
}
