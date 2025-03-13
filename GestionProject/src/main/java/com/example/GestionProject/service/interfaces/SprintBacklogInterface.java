package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.model.SprintBacklog;
import com.example.GestionProject.model.UserStory;

import java.util.List;

public interface SprintBacklogInterface {
    public SprintBacklog getSprintBacklogById(Long sprint_backlog_id);
    public SprintBacklog getSprintBacklogBySprintId(Long sprint_id);
    public SprintBacklog addUserStoryToSprintBacklog(Long sprintId, Long userStoryId);
    public SprintBacklog addUserStoriesToSprintBacklog(Long sprintId, List<Long> userStoryIds);
    public SprintBacklog removeUserStoryFromSprintBacklog(Long sprintId ,Long userStoryId);
    public List<UserStory> getUserStoriesInSprint(Long sprintId);

}
