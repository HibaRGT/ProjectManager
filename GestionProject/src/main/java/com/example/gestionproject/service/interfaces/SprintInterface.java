package com.example.gestionproject.service.interfaces;

import com.example.gestionproject.dto.SprintDTO;

import java.util.List;

public interface SprintInterface {
    public SprintDTO createSprint(SprintDTO sprint);
    public SprintDTO getSprintById(Long id);
    public SprintDTO updateSprint(Long id, SprintDTO sprint);
    public void deleteSprint(Long id);
    public List<SprintDTO> getSprintsByProductBacklogId(Long productBacklog);
}
