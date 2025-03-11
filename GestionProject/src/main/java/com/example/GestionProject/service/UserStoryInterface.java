package com.example.GestionProject.service;

import com.example.GestionProject.model.StatutEnum;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.UserStoryRepository;

import java.util.List;

public interface UserStoryInterface {

    public UserStory createUserStoryInBacklog(UserStory userStory, Long backlogId) ;
    public void deleteUserStoryById(Long id);
    public void deleteUserStoryEntity(UserStory userStory);
    public UserStory getUserStoryById(Long id);
    public List<UserStory> getUserStoriesByBacklogId(Long backlogId);
    public UserStory updateUserStory(Long id, UserStory userStoryDetails);
    public UserStory updateUserStoryStatus(Long id, StatutEnum newStatus);
    public UserStory updateUserStoryPriority(Long id, int newPriority);
}
