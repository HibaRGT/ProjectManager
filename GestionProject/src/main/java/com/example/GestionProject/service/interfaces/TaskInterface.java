package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.model.Task;
import com.example.GestionProject.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskInterface {
    public Task createTask(Task task);
    public Task updateTaskStatus(Long taskId, TaskStatus status);
    public void deleteTask(Long taskId);
    public Task createTaskForUserStory(Long userStoryId, Task task);
    public Task getTaskById(Long taskId);
    public List<Task> getTasksByUserStoryId(Long userStoryId);
    public Task updateTask(Long taskId, Task updatedTask);
    public Task getTasksBySprintId(Long sprintId);
}
