package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.model.Sprint;

import java.util.List;

public interface SprintInterface {
    public Sprint createSprint(Sprint sprint);
    public List<Sprint> getAllSprints();
    public Sprint getSprintById(Long id);
    public Sprint updateSprint(Long id, Sprint sprint);
    public void deleteSprint(Long id);
    public Sprint getSprintsByProjectId(Long projectId);
}
