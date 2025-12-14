package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.TaskDTO;
import com.example.gestionproject.exception.TaskNotFoundException;
import com.example.gestionproject.exception.UserStoryNotFoundException;
import com.example.gestionproject.mapper.TaskMapper;
import com.example.gestionproject.model.Task;
import com.example.gestionproject.model.TaskStatus;
import com.example.gestionproject.model.UserStory;
import com.example.gestionproject.repository.TaskRepository;
import com.example.gestionproject.repository.UserStoryRepository;
import com.example.gestionproject.service.interfaces.TaskInterface;
import com.example.gestionproject.validator.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements TaskInterface {
    private final TaskRepository taskRepository;
    private final UserStoryRepository userStoryRepository;
    private final TaskValidator taskValidator;
    private static final String NOT_FOUND_TASK = "Tâche non trouvée avec l'ID: ";

    @Autowired
    public TaskService(TaskRepository taskRepository, UserStoryRepository userStoryRepository, TaskValidator taskValidator) {
        this.taskRepository = taskRepository;
        this.userStoryRepository = userStoryRepository;
        this.taskValidator = taskValidator;
    }

    @Override
    public TaskDTO createTaskForUserStory(Long userStoryId, TaskDTO taskDTO) {
        taskValidator.validateUserStoryId(userStoryId);
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new UserStoryNotFoundException("UserStory introuvable!"));

        taskValidator.validateUserStoryAssignedToSprint(userStory);

        taskValidator.validateTaskData(taskDTO);

        Task task = TaskMapper.toEntity(taskDTO, userStory);

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        taskValidator.validateTaskId(taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(NOT_FOUND_TASK + taskId));
        return TaskMapper.toDTO(task);
    }

    @Override
    public List<TaskDTO> getTasksByUserStoryId(Long userStoryId) {
        taskValidator.validateUserStoryId(userStoryId);

        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new UserStoryNotFoundException("User Story non trouvée avec l'ID: " + userStoryId));

        List<Task> tasks = userStory.getTasks();
        return tasks.stream().map(TaskMapper::toDTO).toList();
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO updatedTaskDTO) {
        taskValidator.validateTaskId(taskId);

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(NOT_FOUND_TASK + taskId));

        taskValidator.validateTaskData(updatedTaskDTO);

        existingTask.setTitle(updatedTaskDTO.getTitle());
        existingTask.setDescription(updatedTaskDTO.getDescription());

        if(updatedTaskDTO.getStatus() != null && updatedTaskDTO.getStatus() != existingTask.getStatus()){
            updateTaskStatus(taskId, updatedTaskDTO.getStatus());
        }

        Task task = taskRepository.save(existingTask);

        return TaskMapper.toDTO(task);
    }

    public TaskDTO updateTaskStatus(Long taskId, TaskStatus status) {
        taskValidator.validateTaskId(taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Tache introuvable!"));

        task.setStatus(status);

        return TaskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long taskId) {
        taskValidator.validateTaskId(taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(NOT_FOUND_TASK + taskId));

        Optional.ofNullable(task.getUserStory())
                .ifPresent(userStory -> {
                    userStory.getTasks().remove(task);
                    userStoryRepository.save(userStory);
                });

        taskRepository.deleteById(taskId);
    }
}
