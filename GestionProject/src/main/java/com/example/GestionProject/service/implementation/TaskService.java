package com.example.GestionProject.service.implementation;

import com.example.GestionProject.model.Task;
import com.example.GestionProject.model.TaskStatus;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.TaskRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.interfaces.TaskInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements TaskInterface {
    private final TaskRepository taskRepository;
    private final UserStoryRepository userStoryRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserStoryRepository userStoryRepository) {
        this.taskRepository = taskRepository;
        this.userStoryRepository = userStoryRepository;
    }

    public Task createTask(Task task) {
        validateTaskData(task);
        return taskRepository.save(task);
    }

    @Override
    public Task createTaskForUserStory(Long userStoryId, Task task) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable!"));

        if (userStory.getSprintBacklog() == null){
            throw new IllegalStateException("Impossible d'ajouter des tâches à une User Story qui n'est pas dans un Sprint");
        }

        validateTaskData(task);
        task.setUserStory(userStory);

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TO_DO);
        }
        Task savedTask = taskRepository.save(task);

        userStory.getTasks().add(task);
        userStoryRepository.save(userStory);
        return savedTask;
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec l'ID: " + taskId));
    }

    @Override
    public List<Task> getTasksByUserStoryId(Long userStoryId) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("User Story non trouvée avec l'ID: " + userStoryId));

        return userStory.getTasks();
    }

    @Override
    public Task updateTask(Long taskId, Task updatedTask) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec l'ID: " + taskId));

        validateTaskData(updatedTask);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());

        return taskRepository.save(existingTask);
    }

    @Override
    public Task getTasksBySprintId(Long sprintId) {
        return null;
    }

    public Task updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tache introuvable!"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
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
    private void validateTaskData(Task task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la tâche ne peut pas être vide");
        }

        if (task.getDescription() == null || task.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de la tâche ne peut pas être vide");
        }

        if (task.getStatus() == null) {
            throw new IllegalArgumentException("Le status de la tâche ne peut pas être vide");
        }
    }
}
