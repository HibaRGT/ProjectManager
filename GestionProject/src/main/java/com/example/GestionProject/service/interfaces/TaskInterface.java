package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.dto.TaskDTO;
import com.example.GestionProject.model.Task;
import com.example.GestionProject.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskInterface {
    public TaskDTO createTaskForUserStory(Long userStoryId, TaskDTO taskDTO);
    public TaskDTO getTaskById(Long taskId);
    public List<TaskDTO> getTasksByUserStoryId(Long userStoryId);
    public TaskDTO updateTaskStatus(Long taskId, TaskStatus status);
    public void deleteTask(Long taskId);
    public TaskDTO updateTask(Long taskId, TaskDTO updatedTask);
}
