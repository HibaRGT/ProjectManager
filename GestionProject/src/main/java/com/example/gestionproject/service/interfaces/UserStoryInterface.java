package com.example.gestionproject.service.interfaces;

import com.example.gestionproject.dto.UserStoryDTO;
import com.example.gestionproject.model.MoSCoWPriority;
import com.example.gestionproject.model.StatutEnum;

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
    public UserStoryDTO updateUserStoryPriority(Long id, MoSCoWPriority newPriority);
    public UserStoryDTO removeUserStoryFromSprintBacklog(Long sprintBacklogId, Long userStoryId);
    public UserStoryDTO addUserStoryToSprintBacklogById(Long sprintBacklogId, Long userStoryId);
    public UserStoryDTO addUserStoryToEpic(Long epicId, Long userStoryId);
}
