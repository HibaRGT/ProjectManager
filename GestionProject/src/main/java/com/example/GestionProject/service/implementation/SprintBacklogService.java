package com.example.GestionProject.service.implementation;

import com.example.GestionProject.dto.SprintBacklogDTO;
import com.example.GestionProject.dto.SprintDTO;
import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.Sprint;
import com.example.GestionProject.model.SprintBacklog;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.SprintBacklogRepository;
import com.example.GestionProject.repository.SprintRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.interfaces.SprintBacklogInterface;
import com.example.GestionProject.service.interfaces.SprintInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SprintBacklogService implements SprintBacklogInterface{
    private final SprintBacklogRepository sprintBacklogrepository;
    private final SprintRepository sprintRepository;
    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public SprintBacklogService(SprintBacklogRepository sprintBacklogrepository,
                                SprintRepository sprintRepository,
                                ProductBacklogRepository productBacklogRepository
    ) {
        this.sprintBacklogrepository = sprintBacklogrepository;
        this.sprintRepository = sprintRepository;
        this.productBacklogRepository = productBacklogRepository;
    }


    @Override
    public SprintBacklogDTO getSprintBacklogById(Long id) {
        validateSprintBacklogId(id);
        SprintBacklog sp = sprintBacklogrepository.findById(id)
                .orElseThrow(()-> new RuntimeException("SprintBacklog introuvable"));
        return convertToDTO(sp);
    }

    @Override
    public SprintBacklogDTO getSprintBacklogBySprintId(Long id) {
        validateSprintId(id);
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Sprint introuvable"));

        SprintBacklog sp = sprint.getSprintBacklog();

        return convertToDTO(sp);

    }

    @Override
    public SprintBacklogDTO createSprintBacklog(SprintBacklogDTO sprintBacklogDTO) {
        validateSprintBacklogData(sprintBacklogDTO);

        SprintBacklog sprintBacklog = SprintBacklog.builder().build();

        Sprint sprint = sprintRepository.findById(sprintBacklogDTO.getSprintId())
                .orElseThrow(() -> new RuntimeException("Sprint introuvable"));

        ProductBacklog pb = productBacklogRepository.findById(sprintBacklogDTO.getProductBacklogId())
                .orElseThrow(()-> new RuntimeException("ProductBacklog introuvable"));

        sprintBacklog.setSprint(sprint);
        sprintBacklog.setProductBacklog(pb);

        SprintBacklog sp = sprintBacklogrepository.save(sprintBacklog);

        return convertToDTO(sp);
    }

    @Override
    public void deleteSprintBacklogById(Long id) {
        validateSprintBacklogId(id);
        SprintBacklog sp = sprintBacklogrepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SprintBacklog introuvable"));

        sprintBacklogrepository.delete(sp);
    }


    public SprintBacklogDTO convertToDTO(SprintBacklog sprintBLOG){
        return SprintBacklogDTO.builder()
                .id(sprintBLOG.getId())
                .sprintId(sprintBLOG.getSprint() != null ? sprintBLOG.getSprint().getId() : null)
                .userStoryIds(sprintBLOG.getUserStories() != null
                        ? sprintBLOG.getUserStories().stream().map(UserStory::getId).collect(Collectors.toList())
                        : new ArrayList<Long>())
                .productBacklogId(sprintBLOG.getProductBacklog() != null ? sprintBLOG.getProductBacklog().getId() : null)
                .build();
    }

    private void validateSprintBacklogData(SprintBacklogDTO sprintBacklogDTO){
        if (sprintBacklogDTO.getSprintId() == null) {
            throw new IllegalArgumentException("Le sprintBacklog n'est lié à aucun sprint");
        }

        if(sprintBacklogDTO.getProductBacklogId() == null){
            throw new IllegalArgumentException("Le sprintBacklog n'est lié à aucun ProductBacklog");
        }
    }

    private void validateSprintBacklogId(Long id){
        if(id == null){
            throw new RuntimeException("L'ID du sprint backlog ne peut pas etre null");
        }
    }

    private void validateSprintId(Long id){
        if(id == null){
            throw new RuntimeException("L'ID du sprint ne peut pas etre null");
        }
    }
}
