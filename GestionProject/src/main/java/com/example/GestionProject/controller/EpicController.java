package com.example.GestionProject.controller;

import com.example.GestionProject.dto.EpicDTO;
import com.example.GestionProject.dto.UserStoryDTO;
import com.example.GestionProject.model.Epic;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.service.implementation.EpicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/epic")
public class EpicController {
    private final EpicService epicService;

    @Autowired
    public EpicController(EpicService epicService) {
        this.epicService = epicService;
    }

    @PostMapping
    public ResponseEntity<EpicDTO> createEpic(@RequestBody EpicDTO epicDto) {
        EpicDTO createdEpic = epicService.createEpic(epicDto);
        return new ResponseEntity<>(createdEpic, HttpStatus.CREATED);
    }

    @GetMapping("productBacklog/{productBacklogId}")
    public ResponseEntity<List<EpicDTO>> getEpicsByProductBacklogId(@PathVariable Long productBacklogId) {
        List<EpicDTO> epics=epicService.getEpicsByProductBacklogId(productBacklogId);
        return new ResponseEntity<>(epics, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpicDTO> getEpicById(@PathVariable Long id) {
        EpicDTO epic = epicService.getEpicById(id);
        return new ResponseEntity<>(epic, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpicDTO> updateEpic(@PathVariable Long id, @RequestBody EpicDTO epicDto) {
        EpicDTO updatedEpic = epicService.updateEpic(id, epicDto);
        return new ResponseEntity<>(updatedEpic, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpicById(@PathVariable Long id) {
        epicService.deleteEpic(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
