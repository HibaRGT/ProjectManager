package com.example.GestionProject.service;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.StatutEnum;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStoryService {
    private final UserStoryRepository UserStoryRepository;
    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public UserStoryService(UserStoryRepository userStoryRepository, ProductBacklogRepository productBacklogRepository) {
        this.UserStoryRepository = userStoryRepository;
        this.productBacklogRepository = productBacklogRepository;
    }

    public UserStory createUserStoryInBacklog(UserStory userStory, Long backlogId) {
        ProductBacklog backlog = productBacklogRepository.findById(backlogId)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + backlogId));

        userStory.setProductBacklog(backlog);
        validateUserStory(userStory);
        return UserStoryRepository.save(userStory);
    }

    public void deleteUserStoryById(Long id) {
        if (!UserStoryRepository.existsById(id)) {
            throw new RuntimeException("UserStory non trouvée avec l'ID: " + id);
        }
        UserStoryRepository.deleteById(id);
    }

    public void deleteUserStoryEntity(UserStory userStory) {
        UserStoryRepository.delete(userStory);
    }

    public UserStory getUserStoryById(Long id) {
        return UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));
    }

    public List<UserStory> getUserStoriesByBacklogId(Long backlogId) {
        return UserStoryRepository.findByProductBacklogId(backlogId);
    }

    public UserStory updateUserStory(Long id, UserStory userStoryDetails) {
        UserStory us = getUserStoryById(id);

        us.setTitre(userStoryDetails.getTitre());
        us.setDescription(userStoryDetails.getDescription());
        us.setPriorite(userStoryDetails.getPriorite());
        us.setStatut(userStoryDetails.getStatut());

        validateUserStory(us);
        return UserStoryRepository.save(us);
    }

    public UserStory updateUserStoryStatus(Long id, StatutEnum newStatus) {
        UserStory userStory = getUserStoryById(id);
        userStory.setStatut(newStatus);
        return UserStoryRepository.save(userStory);
    }

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
}
