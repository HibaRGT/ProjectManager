package com.example.GestionProject.controller;

import com.example.GestionProject.dto.SprintBacklogDTO;
import com.example.GestionProject.model.SprintBacklog;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.service.implementation.SprintBacklogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprintBacklog")
public class SprintBacklogController {
    private final SprintBacklogService sprintBacklogService;

    @Autowired
    public SprintBacklogController(SprintBacklogService sprintBacklogService) {
        this.sprintBacklogService = sprintBacklogService;
    }

    @PostMapping
    public ResponseEntity<SprintBacklogDTO> createSprintBacklog(@RequestBody SprintBacklogDTO sprintBacklogDTO) {
        SprintBacklogDTO sprintBacklogDTO1=sprintBacklogService.createSprintBacklog(sprintBacklogDTO);
        return new ResponseEntity<>(sprintBacklogDTO1,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintBacklogDTO> getSprintBacklogById(@PathVariable Long id) {
        SprintBacklogDTO sprintBacklog = sprintBacklogService.getSprintBacklogById(id);
        return new ResponseEntity<>(sprintBacklog, HttpStatus.OK);
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<SprintBacklogDTO> getSprintBacklogBySprintId(@PathVariable Long sprintId) {
        SprintBacklogDTO sprintBacklog = sprintBacklogService.getSprintBacklogBySprintId(sprintId);
        return new ResponseEntity<>(sprintBacklog, HttpStatus.OK);
    }

    @DeleteMapping("/{sprintBacklogId}")
    public ResponseEntity<Void> deleteSprintBacklogById(@PathVariable Long sprintBacklogId) {
        sprintBacklogService.deleteSprintBacklogById(sprintBacklogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
