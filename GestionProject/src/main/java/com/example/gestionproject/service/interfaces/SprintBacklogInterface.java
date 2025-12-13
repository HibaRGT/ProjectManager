package com.example.gestionproject.service.interfaces;

import com.example.gestionproject.dto.SprintBacklogDTO;

public interface SprintBacklogInterface {
    public SprintBacklogDTO getSprintBacklogById(Long sprintBacklogId);
    public SprintBacklogDTO getSprintBacklogBySprintId(Long sprintId);
    public SprintBacklogDTO createSprintBacklog(SprintBacklogDTO sprintBacklogDTO);
    public void deleteSprintBacklogById(Long sprintBacklogId);
}
