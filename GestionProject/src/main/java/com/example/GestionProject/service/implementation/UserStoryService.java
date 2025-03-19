package com.example.GestionProject.service.implementation;

import com.example.GestionProject.dto.SprintBacklogDTO;
import com.example.GestionProject.dto.UserStoryDTO;
import com.example.GestionProject.model.*;
import com.example.GestionProject.repository.*;
import com.example.GestionProject.service.interfaces.UserStoryInterface;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserStoryService implements UserStoryInterface {
    private final UserStoryRepository UserStoryRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final SprintBacklogRepository sprintBacklogRepository;
    private final SprintRepository sprintRepository;
    private final EpicRepository epicRepository;

    @Autowired
    public UserStoryService(UserStoryRepository userStoryRepository,
                            ProductBacklogRepository productBacklogRepository,
                            TaskRepository taskRepository,
                            SprintBacklogRepository sprintBacklogRepository,
                            SprintRepository sprintRepository,
                            EpicRepository epicRepository) {
        this.UserStoryRepository = userStoryRepository;
        this.productBacklogRepository = productBacklogRepository;
        this.sprintBacklogRepository = sprintBacklogRepository;
        this.sprintRepository = sprintRepository;
        this.epicRepository = epicRepository;
    }

    @Override
    public UserStoryDTO createUserStoryInBacklog(UserStoryDTO userStoryDTO, Long backlogId) {
        ProductBacklog backlog = productBacklogRepository.findById(backlogId)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + backlogId));

        validateUserStory(userStoryDTO);

        UserStory us = new UserStory();
        us.setTitre(userStoryDTO.getTitre());
        us.setDescription(userStoryDTO.getDescription());
        us.setPriorite(userStoryDTO.getPriorite());
        us.setStatut(userStoryDTO.getStatut() != null ? userStoryDTO.getStatut() : StatutEnum.TO_DO);
        us.setProductBacklog(backlog);

        return convertToDTO(UserStoryRepository.save(us));
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByBacklogId(Long backlogId) {
        List<UserStory> userStories = UserStoryRepository.findByProductBacklogId(backlogId);
        return userStories.stream().map(userStory -> convertToDTO(userStory)).collect(Collectors.toList());
    }

    @Override
    public List<UserStoryDTO> getUserStoriesBySprintBacklogId(Long sprintBacklogId) {
        List<UserStory> userStories = UserStoryRepository.findBySprintBacklogId(sprintBacklogId);
        return userStories.stream().map(userStory -> convertToDTO(userStory)).collect(Collectors.toList());
    }

    @Override
    public UserStoryDTO getUserStoryById(Long id) {
        UserStory us = UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));
        return convertToDTO(us);
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByEpicId(Long epicId) {
        List<UserStory> userStories = UserStoryRepository.findByEpicId(epicId);
        return userStories.stream().map(userStory -> convertToDTO(userStory)).collect(Collectors.toList());
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByTaskId(Long taskId) {
        List<UserStory> userStories = UserStoryRepository.findByTaskId(taskId);
        return userStories.stream().map(userStory -> convertToDTO(userStory)).collect(Collectors.toList());
    }

    @Override
    public UserStoryDTO addUserStoryToSprintBacklogBySprintId(Long sprintId, Long userStoryId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(()-> new RuntimeException("Sprint introuvable"));

        SprintBacklog sprintBacklog = sprint.getSprintBacklog();

        UserStory userStory = UserStoryRepository.findById(userStoryId)
                .orElseThrow(()-> new RuntimeException("UserStory introuvable"));

        if(userStory.getSprintBacklog() != null){
            throw new IllegalArgumentException("Cette User Story est déjà assignée à un Sprint");
        }

        userStory.setSprintBacklog(sprintBacklog);
        sprintBacklog.getUserStories().add(userStory);

        UserStory us = UserStoryRepository.save(userStory);
        sprintBacklogRepository.save(sprintBacklog);
        return convertToDTO(us);
    }

    @Override
    public UserStoryDTO addUserStoryToSprintBacklogBySprintBacklogId(Long sprintBacklogId, Long userStoryId) {
        SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintBacklogId)
                .orElseThrow(()-> new RuntimeException("SprintBacklog introuvable"));

        UserStory userStory = UserStoryRepository.findById(userStoryId)
                .orElseThrow(()-> new RuntimeException("UserStory introuvable"));

        if(userStory.getSprintBacklog() != null){
            throw new IllegalArgumentException("Cette User Story est déjà assignée à un Sprint");
        }

        userStory.setSprintBacklog(sprintBacklog);
        sprintBacklog.getUserStories().add(userStory);

        UserStory us = UserStoryRepository.save(userStory);
        sprintBacklogRepository.save(sprintBacklog);
        return convertToDTO(us);
    }

    @Override
    public UserStoryDTO addUserStoryToEpic(Long epicId, Long userStoryId) {
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new RuntimeException("Aucune Epic trouvée avec l'ID: " + epicId));


        UserStory us = UserStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable"));

        us.setEpic(epic);

        if (epic.getUserStories() == null) {
            epic.setUserStories(new ArrayList<>());
        }

        epic.getUserStories().add(us);

        UserStoryRepository.save(us);
        epicRepository.save(epic);

        return convertToDTO(us);
    }

    @Override
    public UserStoryDTO removeUserStoryFromSprintBacklog(Long sprintBacklogId, Long userStoryId) {
        SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintBacklogId)
                .orElseThrow(() -> new RuntimeException("SprintBacklog non trouvé "));

        UserStory userStory = UserStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("User Story non trouvée avec l'ID: " + userStoryId));

        if (userStory.getSprintBacklog() == null ||
                !userStory.getSprintBacklog().getId().equals(sprintBacklog.getId())) {
            throw new IllegalStateException(
                    "Cette User Story n'est pas assignée à ce Sprint");
        }

        userStory.setSprintBacklog(null);
        sprintBacklog.getUserStories().remove(userStory);

        UserStory us = UserStoryRepository.save(userStory);
        sprintBacklogRepository.save(sprintBacklog);
        return convertToDTO(us);
    }

    @Override
    public UserStoryDTO updateUserStory(Long id, UserStoryDTO userStoryDetails) {
        UserStory us = UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));

        validateUserStory(userStoryDetails);

        us.setTitre(userStoryDetails.getTitre());
        us.setDescription(userStoryDetails.getDescription());

        if(userStoryDetails.getPriorite() != 0 && userStoryDetails.getPriorite() != us.getPriorite()) {
            updateUserStoryPriority(id, us.getPriorite());
        }

        if(userStoryDetails.getStatut() != null && userStoryDetails.getStatut() != us.getStatut()){
            updateUserStoryStatus(id, us.getStatut());
        }

        return convertToDTO(UserStoryRepository.save(us));
    }

    @Override
    public UserStoryDTO updateUserStoryStatus(Long id, StatutEnum newStatus) {
        UserStory us = UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));

        us.setStatut(newStatus);

        return convertToDTO(UserStoryRepository.save(us));
    }

    @Override
    public UserStoryDTO updateUserStoryPriority(Long id, int newPriority) {
        UserStory us = UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));

        us.setPriorite(newPriority);
        return convertToDTO(UserStoryRepository.save(us));
    }

    @Override
    public void deleteUserStoryById(Long id) {
        if (!UserStoryRepository.existsById(id)) {
            throw new RuntimeException("UserStory non trouvée avec l'ID: " + id);
        }
        UserStoryRepository.deleteById(id);
    }


    public void validateUserStory(UserStoryDTO userStoryDTO) {
        if (userStoryDTO.getTitre() == null || userStoryDTO.getTitre().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la user story ne peut pas être vide");
        }
        if (userStoryDTO.getDescription() == null || userStoryDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de la user story ne peut pas être vide");
        }
    }
/////////////////////////////////////////:

    public UserStoryDTO convertToDTO(UserStory userStory) {
        return new UserStoryDTO(
                userStory.getId(),
                userStory.getTitre(),
                userStory.getDescription(),
                userStory.getPriorite(),
                userStory.getStatut(),
                userStory.getEpic() != null ? userStory.getEpic().getId() : null,
                userStory.getProductBacklog() != null ? userStory.getProductBacklog().getId() : null,
                userStory.getSprintBacklog() != null ? userStory.getSprintBacklog().getId() : null,
                userStory.getTasks() != null ?
                        userStory.getTasks().stream().map(Task::getId).collect(Collectors.toList())
                        : null
        );
    }
}
