package com.example.gestionproject.service.interfaces;

import com.example.gestionproject.dto.TaskDTO;
import com.example.gestionproject.model.TaskStatus;

import java.util.List;

public interface TaskInterface {
    public TaskDTO createTaskForUserStory(Long userStoryId, TaskDTO taskDTO);
    public TaskDTO getTaskById(Long taskId);
    public List<TaskDTO> getTasksByUserStoryId(Long userStoryId);
    public TaskDTO updateTaskStatus(Long taskId, TaskStatus status);
    public void deleteTask(Long taskId);
    public TaskDTO updateTask(Long taskId, TaskDTO updatedTask);
}
