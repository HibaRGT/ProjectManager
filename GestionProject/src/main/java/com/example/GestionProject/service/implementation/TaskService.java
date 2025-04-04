package com.example.GestionProject.service.implementation;

import com.example.GestionProject.dto.TaskDTO;
import com.example.GestionProject.dto.UserStoryDTO;
import com.example.GestionProject.model.Task;
import com.example.GestionProject.model.TaskStatus;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.TaskRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.interfaces.TaskInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService implements TaskInterface {
    private final TaskRepository taskRepository;
    private final UserStoryRepository userStoryRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserStoryRepository userStoryRepository) {
        this.taskRepository = taskRepository;
        this.userStoryRepository = userStoryRepository;
    }

    @Override
    public TaskDTO createTaskForUserStory(Long userStoryId, TaskDTO taskDTO) {
        validateUserStoryId(userStoryId);
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable!"));

        if (userStory.getSprintBacklog() == null){
            throw new IllegalStateException("Impossible d'ajouter des tâches à une User Story qui n'est pas dans un Sprint");
        }

        validateTaskData(taskDTO);

        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .status(taskDTO.getStatus() != null ? taskDTO.getStatus() : TaskStatus.TO_DO)
                .userStory(userStory)
                .build();

        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        validateTaskId(taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec l'ID: " + taskId));
        return convertToDTO(task);
    }

    @Override
    public List<TaskDTO> getTasksByUserStoryId(Long userStoryId) {
        validateUserStoryId(userStoryId);

        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("User Story non trouvée avec l'ID: " + userStoryId));

        List<Task> tasks = userStory.getTasks();
        return tasks.stream().map(task -> convertToDTO(task)).collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO updatedTaskDTO) {
        validateTaskId(taskId);

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec l'ID: " + taskId));

        validateTaskData(updatedTaskDTO);

        existingTask.setTitle(updatedTaskDTO.getTitle());
        existingTask.setDescription(updatedTaskDTO.getDescription());

        if(updatedTaskDTO.getStatus() != null && updatedTaskDTO.getStatus() != existingTask.getStatus()){
            updateTaskStatus(taskId, updatedTaskDTO.getStatus());
        }

        Task task = taskRepository.save(existingTask);

        return convertToDTO(task);
    }

    public TaskDTO updateTaskStatus(Long taskId, TaskStatus status) {
        validateTaskId(taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tache introuvable!"));

        task.setStatus(status);

        return convertToDTO(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long taskId) {
        validateTaskId(taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec l'ID: " + taskId));

        UserStory userStory = task.getUserStory();
        if (userStory != null) {
            userStory.getTasks().remove(task);
            userStoryRepository.save(userStory);
        }

        taskRepository.deleteById(taskId);
    }


    //validation
    private void validateTaskData(TaskDTO taskDTO) {
        if (taskDTO.getTitle() == null || taskDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la tâche ne peut pas être vide");
        }

        if (taskDTO.getDescription() == null || taskDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de la tâche ne peut pas être vide");
        }

        if (taskDTO.getStatus() == null) {
            throw new IllegalArgumentException("Le status de la tâche ne peut pas être vide");
        }
    }

    private void validateTaskId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du task ne peut pas etre null");
        }
    }

    private void validateUserStoryId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du userStory ne peut pas etre null");
        }
    }


    public TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .userStoryId( task.getUserStory() != null ? task.getUserStory().getId() : null)
                .build();
    }
}
