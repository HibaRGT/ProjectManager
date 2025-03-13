package com.example.GestionProject.service.implementation;

import com.example.GestionProject.model.Sprint;
import com.example.GestionProject.model.SprintBacklog;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.SprintBacklogRepository;
import com.example.GestionProject.repository.SprintRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.interfaces.SprintBacklogInterface;
import com.example.GestionProject.service.interfaces.SprintInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprintBacklogService implements SprintBacklogInterface{
    private final SprintBacklogRepository sprintBacklogrepository;
    private final UserStoryRepository userStoryRepository;
    private final SprintRepository sprintRepository;

    @Autowired
    public SprintBacklogService(SprintBacklogRepository sprintBacklogrepository, UserStoryRepository userStoryRepository, SprintRepository sprintRepository) {
        this.sprintBacklogrepository = sprintBacklogrepository;
        this.userStoryRepository = userStoryRepository;
        this.sprintRepository = sprintRepository;
    }


    @Override
    public SprintBacklog getSprintBacklogById(Long sprint_backlog_id) {
        return sprintBacklogrepository.findById(sprint_backlog_id)
                .orElseThrow(()-> new RuntimeException("SprintBacklog introuvable"));
    }

    @Override
    public SprintBacklog getSprintBacklogBySprintId(Long sprint_id) {
        Sprint sprint = sprintRepository.findById(sprint_id)
                .orElseThrow(()-> new RuntimeException("Sprint introuvable"));

        return sprint.getSprintBacklog();

    }

    @Override
    public SprintBacklog addUserStoryToSprintBacklog(Long sprintId, Long userStoryId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(()-> new RuntimeException("Sprint introuvable"));

        SprintBacklog sprintBacklog = sprint.getSprintBacklog();

        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(()-> new RuntimeException("UserStory introuvable"));

        if(userStory.getSprintBacklog() != null){
            throw new IllegalArgumentException("Cette User Story est déjà assignée à un Sprint");
        }

        userStory.setSprintBacklog(sprintBacklog);
        sprintBacklog.getUserStories().add(userStory);

        userStoryRepository.save(userStory);
        return sprintBacklogrepository.save(sprintBacklog);
    }

    @Override
    public SprintBacklog addUserStoriesToSprintBacklog(Long sprintId, List<Long> userStoryIds) {
        return null;
    }


    @Override
    public SprintBacklog removeUserStoryFromSprintBacklog(Long sprintId, Long userStoryId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint non trouvé avec l'ID: " + sprintId));

        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("User Story non trouvée avec l'ID: " + userStoryId));

        SprintBacklog sprintBacklog = sprint.getSprintBacklog();

        if (userStory.getSprintBacklog() == null ||
                !userStory.getSprintBacklog().getId().equals(sprintBacklog.getId())) {
            throw new IllegalStateException(
                    "Cette User Story n'est pas assignée à ce Sprint");
        }

        userStory.setSprintBacklog(null);
        sprintBacklog.getUserStories().remove(userStory);

        // Save changes
        userStoryRepository.save(userStory);
        return sprintBacklogrepository.save(sprintBacklog);
    }

    @Override
    public List<UserStory> getUserStoriesInSprint(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint est introuvable"));

        return sprint.getSprintBacklog().getUserStories();
    }
}
