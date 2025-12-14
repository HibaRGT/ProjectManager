package com.example.gestionproject.mapper;

import com.example.gestionproject.dto.UserStoryDTO;
import com.example.gestionproject.model.ProductBacklog;
import com.example.gestionproject.model.StatutEnum;
import com.example.gestionproject.model.Task;
import com.example.gestionproject.model.UserStory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserStoryMapper {

    private UserStoryMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UserStoryDTO toDTO(UserStory userStory) {
        if (userStory == null) {
            return null;
        }

        return UserStoryDTO.builder()
                .id(userStory.getId())
                .titre(userStory.getTitre())
                .description(userStory.getDescription())
                .valeurMetier(userStory.getValeurMetier())
                .urgence(userStory.getUrgence())
                .complexite(userStory.getComplexite())
                .risques(userStory.getRisques())
                .dependances(userStory.getDependances())
                .statut(userStory.getStatut())
                .priorite(userStory.getPriorite())
                .epicId(userStory.getEpic() != null ? userStory.getEpic().getId() : null)
                .productBacklogId(userStory.getProductBacklog() != null ? userStory.getProductBacklog().getId() : null)
                .sprintBacklogId(userStory.getSprintBacklog() != null ? userStory.getSprintBacklog().getId() : null)
                .taskIds(userStory.getTasks() != null
                        ? userStory.getTasks().stream().map(Task::getId).collect(Collectors.toCollection(ArrayList::new))
                        : new ArrayList<>())
                .build();
    }

    public static List<UserStoryDTO> toDTOList(List<UserStory> userStories) {
        if (userStories == null) return new ArrayList<>();
        return userStories.stream()
                .map(UserStoryMapper::toDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static UserStory toEntity(UserStoryDTO dto, ProductBacklog backlog) {
        return UserStory.builder()
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .valeurMetier(dto.getValeurMetier())
                .urgence(dto.getUrgence())
                .complexite(dto.getComplexite())
                .risques(dto.getRisques())
                .dependances(dto.getDependances())
                .statut(getStatusOrDefault(dto.getStatut()))
                .productBacklog(backlog)
                .build();
    }

    private static StatutEnum getStatusOrDefault(StatutEnum statut) {
        return statut != null ? statut : StatutEnum.TO_DO;
    }
}