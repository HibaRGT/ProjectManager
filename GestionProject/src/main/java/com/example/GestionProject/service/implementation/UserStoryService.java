package com.example.GestionProject.service.implementation;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.StatutEnum;
import com.example.GestionProject.model.Task;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.TaskRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.interfaces.UserStoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStoryService implements UserStoryInterface {
    private final UserStoryRepository UserStoryRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public UserStoryService(UserStoryRepository userStoryRepository, ProductBacklogRepository productBacklogRepository, TaskRepository taskRepository) {
        this.UserStoryRepository = userStoryRepository;
        this.productBacklogRepository = productBacklogRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public UserStory createUserStoryInBacklog(UserStory userStory, Long backlogId) {
        ProductBacklog backlog = productBacklogRepository.findById(backlogId)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + backlogId));

        userStory.setProductBacklog(backlog);
        validateUserStory(userStory);
        return UserStoryRepository.save(userStory);
    }

    @Override
    public void deleteUserStoryById(Long id) {
        if (!UserStoryRepository.existsById(id)) {
            throw new RuntimeException("UserStory non trouvée avec l'ID: " + id);
        }
        UserStoryRepository.deleteById(id);
    }

    @Override
    public UserStory getUserStoryById(Long id) {
        return UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));
    }

    @Override
    public List<UserStory> getUserStoriesByBacklogId(Long backlogId) {
        return UserStoryRepository.findByProductBacklogId(backlogId);
    }

    @Override
    public UserStory updateUserStory(Long id, UserStory userStoryDetails) {
        UserStory us = getUserStoryById(id);

        us.setTitre(userStoryDetails.getTitre());
        us.setDescription(userStoryDetails.getDescription());
        us.setPriorite(userStoryDetails.getPriorite());
        us.setStatut(userStoryDetails.getStatut());

        validateUserStory(us);
        return UserStoryRepository.save(us);
    }

    @Override
    public UserStory updateUserStoryStatus(Long id, StatutEnum newStatus) {
        UserStory userStory = getUserStoryById(id);
        userStory.setStatut(newStatus);
        return UserStoryRepository.save(userStory);
    }

    @Override
    public UserStory updateUserStoryPriority(Long id, int newPriority) {
        UserStory userStory = getUserStoryById(id);
        userStory.setPriorite(newPriority);
        return UserStoryRepository.save(userStory);
    }


    private void validateUserStory(UserStory userStory) {
        if (userStory.getTitre() == null || userStory.getTitre().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la user story ne peut pas être vide");
        }
        if (userStory.getDescription() == null || userStory.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de la user story ne peut pas être vide");
        }
    }
/////////////////////////////////////////:
    @Override
    public UserStory addTaskToUserStory(Long userStoryId, Long taskId) {
        UserStory userStory = UserStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("User Story not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        userStory.getTasks().add(task);
        task.setUserStory(userStory);
        taskRepository.save(task);
        return UserStoryRepository.save(userStory);
    }
//////////////////////////////////////////
    @Override
    public UserStory removeTaskFromUserStory(Long userStoryId, Long taskId) {
        UserStory userStory = UserStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("User Story not found"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        userStory.getTasks().remove(task);
        task.getUserStory().getTasks().remove(task);
        return UserStoryRepository.save(userStory);
    }
}
