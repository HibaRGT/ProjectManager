package com.example.GestionProject.service.implementation;

import com.example.GestionProject.model.Sprint;
import com.example.GestionProject.model.SprintBacklog;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.SprintBacklogRepository;
import com.example.GestionProject.repository.SprintRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.interfaces.SprintInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class SprintService implements SprintInterface {
    private final SprintRepository sprintRepository;
    private final UserStoryRepository userStoryRepository;
    private final SprintBacklogRepository sprintBacklogRepository;

    @Autowired
    public SprintService(SprintRepository sprintRepository, UserStoryRepository userStoryRepository, SprintBacklogRepository sprintBacklogRepository) {
        this.sprintRepository = sprintRepository;
        this.userStoryRepository = userStoryRepository;
        this.sprintBacklogRepository = sprintBacklogRepository;
    }

    @Override
    public Sprint createSprint(Sprint sprint) {
        validateSprintData(sprint);

        SprintBacklog sprintBacklog = new SprintBacklog();
        sprintBacklog.setSprint(sprint);
        sprint.setSprintBacklog(sprintBacklog);
        sprintBacklogRepository.save(sprintBacklog);

        return sprintRepository.save(sprint);
    }

    @Override
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    @Override
    public Sprint getSprintById(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint non trouvé avec l'ID: " + id));
    }

    @Override
    public Sprint updateSprint(Long id, Sprint sprint) {
        Sprint existingSprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint non trouvé avec l'ID: " + id));
        validateSprintData(sprint);

        existingSprint.setName(sprint.getName());
        existingSprint.setDescription(sprint.getDescription());
        existingSprint.setStartDate(sprint.getStartDate());
        existingSprint.setEndDate(sprint.getEndDate());

        return sprintRepository.save(existingSprint);
    }

    @Override
    public void deleteSprint(Long id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint non trouvé avec l'ID: " + id));
        sprintRepository.delete(sprint);
    }

    @Override
    public Sprint getSprintsByProjectId(Long projectId) {
        return null;
    }

    private void validateSprintData(Sprint sprint) {
        if (sprint.getName() == null || sprint.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom du sprint ne peut pas être vide");
        }

        if (sprint.getStartDate() == null) {
            throw new IllegalArgumentException("La date de début du sprint est obligatoire");
        }

        if (sprint.getEndDate() == null) {
            throw new IllegalArgumentException("La date de fin du sprint est obligatoire");
        }

        if (sprint.getEndDate().isBefore(sprint.getStartDate())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }

        LocalDate start = sprint.getStartDate();
        LocalDate end = sprint.getEndDate();
        long daysBetween = ChronoUnit.DAYS.between(start, end);

        if (daysBetween < 7) {
            throw new IllegalArgumentException("La durée du sprint ne peut pas être inférieure à 7 jours");
        }

        if (daysBetween > 30) {
            throw new IllegalArgumentException("La durée du sprint ne peut pas dépasser 30 jours");
        }
    }


}
