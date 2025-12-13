package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.SprintDTO;
import com.example.gestionproject.model.Sprint;
import com.example.gestionproject.model.SprintBacklog;
import com.example.gestionproject.repository.SprintBacklogRepository;
import com.example.gestionproject.repository.SprintRepository;
import com.example.gestionproject.service.interfaces.SprintInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SprintService implements SprintInterface {
    private final SprintRepository sprintRepository;
    private final SprintBacklogRepository sprintBacklogRepository;


    @Autowired
    public SprintService(SprintRepository sprintRepository
    , SprintBacklogRepository sprintBacklogRepository) {
        this.sprintRepository = sprintRepository;
        this.sprintBacklogRepository = sprintBacklogRepository;
    }

    @Override
    public SprintDTO createSprint(SprintDTO sprintDTO) {

        validateSprintData(sprintDTO);

        Sprint sprint = Sprint.builder()
                .name(sprintDTO.getName())
                .description(sprintDTO.getDescription())
                .startDate(sprintDTO.getStartDate())
                .endDate(sprintDTO.getEndDate())
                .build();

        Sprint savedSprint = sprintRepository.save(sprint);

        return convertToDTO((savedSprint));
    }

    @Override
    public SprintDTO getSprintById(Long id) {
        validateSprintId(id);
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint non trouvé avec l'ID: " + id));
        return convertToDTO(sprint);
    }

    @Override
    public SprintDTO updateSprint(Long id, SprintDTO sprintDetails) {
        validateSprintId(id);
        Sprint existingSprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint non trouvé avec l'ID: " + id));

        validateSprintData(sprintDetails);

        existingSprint.setName(sprintDetails.getName());
        existingSprint.setDescription(sprintDetails.getDescription());
        existingSprint.setStartDate(sprintDetails.getStartDate());
        existingSprint.setEndDate(sprintDetails.getEndDate());

        return convertToDTO(sprintRepository.save(existingSprint));
    }

    public List<SprintDTO> getSprintsByProductBacklogId(Long productBacklogId) {
        List<SprintBacklog> sprintBacklogs = sprintBacklogRepository.findByProductBacklogId(productBacklogId);
        return sprintBacklogs.stream()
                .map(SprintBacklog::getSprint)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSprint(Long id) {
        validateSprintId(id);

        if(!sprintRepository.existsById(id)) {
            throw new RuntimeException("Sprint non trouvée avec l'ID: "+ id);
        }
        sprintRepository.deleteById(id);
    }


    private void validateSprintData(SprintDTO sprint) {
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

    private void validateSprintId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du sprint ne peut pas être null");
        }
    }

    public SprintDTO convertToDTO(Sprint sprint) {
        SprintDTO dto = SprintDTO.builder()
                .id(sprint.getId())
                .name(sprint.getName())
                .description(sprint.getDescription())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .build();

        if (sprint.getSprintBacklog() != null) {
            dto.setSprintBacklogId(sprint.getSprintBacklog().getId());
        }

        return dto;
    }


}
