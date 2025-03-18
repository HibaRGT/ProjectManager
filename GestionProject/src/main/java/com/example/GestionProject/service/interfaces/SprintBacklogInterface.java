package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.dto.SprintBacklogDTO;
import com.example.GestionProject.model.SprintBacklog;
import com.example.GestionProject.model.UserStory;

import java.util.List;

public interface SprintBacklogInterface {
    public SprintBacklogDTO getSprintBacklogById(Long sprint_backlog_id);
    public SprintBacklogDTO getSprintBacklogBySprintId(Long sprint_id);
    public SprintBacklogDTO createSprintBacklog(SprintBacklogDTO sprintBacklogDTO);
    public void deleteSprintBacklogById(Long sprint_backlog_id);

}
