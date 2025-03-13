package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.model.StatutEnum;
import com.example.GestionProject.model.UserStory;

import java.util.List;

public interface UserStoryInterface {
    public UserStory createUserStoryInBacklog(UserStory userStory, Long backlogId) ;
    public void deleteUserStoryById(Long id);
    public UserStory getUserStoryById(Long id);
    public List<UserStory> getUserStoriesByBacklogId(Long backlogId);
    public UserStory updateUserStory(Long id, UserStory userStoryDetails);
    public UserStory updateUserStoryStatus(Long id, StatutEnum newStatus);
    public UserStory updateUserStoryPriority(Long id, int newPriority);
    public UserStory addTaskToUserStory(Long userStoryId, Long taskId);
    public UserStory removeTaskFromUserStory(Long userStoryId, Long taskId);
}
