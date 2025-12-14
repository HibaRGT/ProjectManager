package com.example.gestionproject.mapper;

import com.example.gestionproject.dto.SprintBacklogDTO;
import com.example.gestionproject.model.SprintBacklog;
import com.example.gestionproject.model.UserStory;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SprintBacklogMapper {

    private SprintBacklogMapper(){
        throw new IllegalStateException("Utility class");
    }

    public static SprintBacklogDTO toDTO(SprintBacklog sprintBLOG){
        return SprintBacklogDTO.builder()
                .id(sprintBLOG.getId())
                .sprintId(sprintBLOG.getSprint() != null ? sprintBLOG.getSprint().getId() : null)
                .userStoryIds(sprintBLOG.getUserStories() != null
                        ? sprintBLOG.getUserStories().stream().map(UserStory::getId).collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>())
                .productBacklogId(sprintBLOG.getProductBacklog() != null ? sprintBLOG.getProductBacklog().getId() : null)
                .build();
    }

}
