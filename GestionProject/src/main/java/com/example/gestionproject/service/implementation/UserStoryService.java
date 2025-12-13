package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.UserStoryDTO;
import com.example.gestionproject.model.*;
import com.example.gestionproject.repository.*;
import com.example.gestionproject.service.interfaces.UserStoryInterface;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserStoryService implements UserStoryInterface {
    private final UserStoryRepository userStoryRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final SprintBacklogRepository sprintBacklogRepository;
    private final EpicRepository epicRepository;
    private final TaskRepository taskRepository;
    private static final String NOT_FOUND_PROJECT= "UserStory introuvable avec l'ID: ";

    @Autowired
    public UserStoryService(UserStoryRepository userStoryRepository,
                            ProductBacklogRepository productBacklogRepository,
                            SprintBacklogRepository sprintBacklogRepository,
                            EpicRepository epicRepository,
                            TaskRepository taskRepository) {
        this.userStoryRepository = userStoryRepository;
        this.productBacklogRepository = productBacklogRepository;
        this.sprintBacklogRepository = sprintBacklogRepository;
        this.epicRepository = epicRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public UserStoryDTO createUserStoryInBacklog(UserStoryDTO userStoryDTO, Long backlogId) {
        validateBacklogId(backlogId);

        ProductBacklog backlog = productBacklogRepository.findById(backlogId)
                .orElseThrow(() -> new RuntimeException("ProductBacklog non trouvé avec l'ID: " + backlogId));

        validateUserStory(userStoryDTO);

        UserStory us = UserStory.builder()
                .titre(userStoryDTO.getTitre())
                .description(userStoryDTO.getDescription())
                .valeurMetier(userStoryDTO.getValeurMetier())
                .urgence(userStoryDTO.getUrgence())
                .complexite(userStoryDTO.getComplexite())
                .risques(userStoryDTO.getRisques())
                .dependances(userStoryDTO.getDependances())
                .statut(userStoryDTO.getStatut() != null ? userStoryDTO.getStatut() : StatutEnum.TO_DO)
                .productBacklog(backlog)
                .build();

        int note = calculateNote(us);
        us.setNotePriorite(note);
        us.setPriorite(associatePriorite(note));

        return convertToDTO(userStoryRepository.save(us));
    }

    private int calculateNote(UserStory s) {
        return (s.getValeurMetier() * 3)
                + (s.getUrgence() * 2)
                - s.getComplexite()
                - s.getRisques()
                + s.getDependances();
    }

    private MoSCoWPriority associatePriorite(int note) {
        if (note >= 18) return MoSCoWPriority.MUST_HAVE;
        else if (note >= 13) return MoSCoWPriority.SHOULD_HAVE;
        else if (note >= 8) return MoSCoWPriority.COULD_HAVE;
        else return MoSCoWPriority.WONT_HAVE;
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByBacklogId(Long backlogId) {
        validateBacklogId(backlogId);
        List<UserStory> userStories = userStoryRepository.findByProductBacklogId(backlogId);
        return userStories.stream()
                          .map(this::convertToDTO)
                          .toList();
    }

    @Override
    public List<UserStoryDTO> getUserStoriesBySprintBacklogId(Long sprintBacklogId) {
        validateSprintBacklogId(sprintBacklogId);
        List<UserStory> userStories = userStoryRepository.findBySprintBacklogId(sprintBacklogId);
        return userStories.stream().map(this::convertToDTO).toList();
    }

    @Override
    public UserStoryDTO getUserStoryById(Long id) {
        validateUserStoryId(id);
        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_PROJECT + id));
        return convertToDTO(us);
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByEpicId(Long epicId) {
        validateEpicId(epicId);
        List<UserStory> userStories = userStoryRepository.findByEpicId(epicId);
        return userStories.stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByTaskId(Long taskId) {
        validateTaskId(taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        UserStory userStory = task.getUserStory();

        return List.of(convertToDTO(userStory));
    }

    @Override
    public UserStoryDTO addUserStoryToSprintBacklogById(Long sprintBacklogId, Long userStoryId) {
        validateSprintBacklogId(sprintBacklogId);
        validateUserStoryId(userStoryId);

        SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintBacklogId)
                .orElseThrow(()-> new RuntimeException("SprintBacklog introuvable"));

        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(()-> new RuntimeException("UserStory introuvable"));

        if(userStory.getSprintBacklog() != null){
            throw new IllegalArgumentException("Cette User Story est déjà assignée à un Sprint");
        }

        userStory.setSprintBacklog(sprintBacklog);
        sprintBacklog.getUserStories().add(userStory);

        UserStory us = userStoryRepository.save(userStory);
        sprintBacklogRepository.save(sprintBacklog);
        return convertToDTO(us);
    }

    @Override
    public UserStoryDTO addUserStoryToEpic(Long epicId, Long userStoryId) {
        validateEpicId(epicId);
        validateUserStoryId(userStoryId);

        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new RuntimeException("Aucune Epic trouvée avec l'ID: " + epicId));


        UserStory us = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable"));

        us.setEpic(epic);

        if (epic.getUserStories() == null) {
            epic.setUserStories(new ArrayList<>());
        }

        epic.getUserStories().add(us);

        userStoryRepository.save(us);
        epicRepository.save(epic);

        return convertToDTO(us);
    }

    @Override
    public UserStoryDTO removeUserStoryFromSprintBacklog(Long sprintBacklogId, Long userStoryId) {
        validateSprintId(sprintBacklogId);
        validateUserStoryId(userStoryId);

        SprintBacklog sprintBacklog = sprintBacklogRepository.findById(sprintBacklogId)
                .orElseThrow(() -> new RuntimeException("SprintBacklog non trouvé "));

        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new RuntimeException("User Story non trouvée avec l'ID: " + userStoryId));

        if (userStory.getSprintBacklog() == null ||
                !userStory.getSprintBacklog().getId().equals(sprintBacklog.getId())) {
            throw new IllegalStateException(
                    "Cette User Story n'est pas assignée à ce Sprint");
        }

        userStory.setSprintBacklog(null);
        sprintBacklog.getUserStories().remove(userStory);

        UserStory us = userStoryRepository.save(userStory);
        sprintBacklogRepository.save(sprintBacklog);
        return convertToDTO(us);
    }

    @Override
    public UserStoryDTO updateUserStory(Long id, UserStoryDTO userStoryDetails) {
        validateUserStoryId(id);

        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_PROJECT + id));

        validateUserStory(userStoryDetails);

        us.setTitre(userStoryDetails.getTitre());
        us.setDescription(userStoryDetails.getDescription());
        us.setValeurMetier(userStoryDetails.getValeurMetier());
        us.setUrgence(userStoryDetails.getUrgence());
        us.setComplexite(userStoryDetails.getComplexite());
        us.setRisques(userStoryDetails.getRisques());
        us.setDependances(userStoryDetails.getDependances());

        int note = calculateNote(us);
        MoSCoWPriority nouvellePriorite = associatePriorite(note);
        us.setPriorite(nouvellePriorite);

        if(userStoryDetails.getStatut() != null && userStoryDetails.getStatut() != us.getStatut()){
            updateUserStoryStatus(id, userStoryDetails.getStatut());
        }

        return convertToDTO(userStoryRepository.save(us));
    }

    @Override
    public UserStoryDTO updateUserStoryStatus(Long id, StatutEnum newStatus) {
        validateUserStoryId(id);

        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_PROJECT + id));

        us.setStatut(newStatus);

        return convertToDTO(userStoryRepository.save(us));
    }

    @Override
    public UserStoryDTO updateUserStoryPriority(Long id, MoSCoWPriority newPriority) {
        validateUserStoryId(id);
        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_PROJECT + id));

        us.setPriorite(newPriority);
        return convertToDTO(userStoryRepository.save(us));
    }

    @Override
    public void deleteUserStoryById(Long id) {
        validateUserStoryId(id);
        if (!userStoryRepository.existsById(id)) {
            throw new EntityNotFoundException("UserStory non trouvée avec l'ID: " + id);
        }
        userStoryRepository.deleteById(id);
    }

    public void validateUserStory(UserStoryDTO userStoryDTO) {
        if (userStoryDTO.getTitre() == null || userStoryDTO.getTitre().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la user story ne peut pas être vide");
        }
        if (userStoryDTO.getDescription() == null || userStoryDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de la user story ne peut pas être vide");
        }
    }

    private void validateUserStoryId(Long id){
        if (id == null) {
            throw new IllegalArgumentException("L'ID du userStory ne peut pas être null");
        }
    }

    private void validateEpicId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID de l'Epic ne peut pas être null");
        }
    }

    private void validateTaskId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du Task ne peut pas être null");
        }
    }

    private void validateBacklogId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du backlog ne peut pas être null");
        }
    }

    private void validateSprintId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du sprint ne peut pas être null");
        }
    }

    private void validateSprintBacklogId(Long id){
        if(id == null){
            throw new IllegalArgumentException("L'ID du sprint backlog ne peut pas être null");
        }
    }


    public UserStoryDTO convertToDTO(UserStory userStory) {
        return UserStoryDTO.builder()
                .id(userStory.getId())
                .titre(userStory.getTitre())
                .description(userStory.getDescription())
                .valeurMetier(userStory.getValeurMetier())
                .risques(userStory.getRisques())
                .urgence(userStory.getUrgence())
                .dependances(userStory.getDependances())
                .complexite(userStory.getComplexite())
                .statut(userStory.getStatut())
                .priorite(userStory.getPriorite())
                .epicId(userStory.getEpic() != null ? userStory.getEpic().getId() : null)
                .productBacklogId(userStory.getProductBacklog() != null ? userStory.getProductBacklog().getId() : null)
                .sprintBacklogId(userStory.getSprintBacklog() != null ? userStory.getSprintBacklog().getId() : null)
                .taskIds(userStory.getTasks() != null ?
                        userStory.getTasks().stream().map(Task::getId).collect(toList())
                        : null
                ).build();
    }
}
