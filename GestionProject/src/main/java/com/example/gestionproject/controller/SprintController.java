package com.example.gestionproject.controller;

import com.example.gestionproject.dto.SprintDTO;
import com.example.gestionproject.service.implementation.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprint")
public class SprintController {
    private final SprintService sprintService;

    @Autowired
    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @PostMapping
    public ResponseEntity<SprintDTO> createSprint(@RequestBody SprintDTO sprintDto) {
        SprintDTO createdSprint = sprintService.createSprint(sprintDto);
        return new ResponseEntity<>(createdSprint, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintDTO> getSprintById(@PathVariable Long id) {
        SprintDTO sprint = sprintService.getSprintById(id);
        return new ResponseEntity<>(sprint, HttpStatus.OK);
    }

    @GetMapping("/productBacklog/{productBacklogId}")
    public ResponseEntity<List<SprintDTO>> getSprintsByProductBacklogId(@PathVariable Long productBacklogId) {
        List<SprintDTO> sprints=sprintService.getSprintsByProductBacklogId(productBacklogId);
        return new ResponseEntity<>(sprints, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintDTO> updateSprint(@PathVariable Long id, @RequestBody SprintDTO sprintDto) {
        SprintDTO updatedSprint = sprintService.updateSprint(id, sprintDto);
        return new ResponseEntity<>(updatedSprint, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        sprintService.deleteSprint(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
