package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.dto.UserStoryDTO;
import com.example.GestionProject.model.StatutEnum;
import com.example.GestionProject.model.UserStory;

import java.util.List;

public interface UserStoryInterface {
    public UserStoryDTO createUserStoryInBacklog(UserStoryDTO userStoryDTO, Long backlogId) ;
    public void deleteUserStoryById(Long id);
    public UserStoryDTO getUserStoryById(Long id);
    public List<UserStoryDTO> getUserStoriesByBacklogId(Long backlogId);
    public List<UserStoryDTO> getUserStoriesByEpicId(Long epicId);
    public List<UserStoryDTO> getUserStoriesByTaskId(Long taskId);
    public List<UserStoryDTO> getUserStoriesBySprintBacklogId(Long sprintBacklogId);
    public UserStoryDTO updateUserStory(Long id, UserStoryDTO userStoryDetails);
    public UserStoryDTO updateUserStoryStatus(Long id, StatutEnum newStatus);
    public UserStoryDTO updateUserStoryPriority(Long id, int newPriority);
    public UserStoryDTO removeUserStoryFromSprintBacklog(Long sprintBacklogId, Long userStoryId);
    public UserStoryDTO addUserStoryToSprintBacklogBySprintBacklogId(Long sprintBacklogId, Long userStoryId);
    public UserStoryDTO addUserStoryToSprintBacklogBySprintId(Long sprintId, Long userStoryId);
}
