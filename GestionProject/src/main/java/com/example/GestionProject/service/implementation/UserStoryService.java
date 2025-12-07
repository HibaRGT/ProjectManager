package com.example.GestionProject.service.implementation;

import com.example.GestionProject.dto.SprintBacklogDTO;
import com.example.GestionProject.dto.UserStoryDTO;
import com.example.GestionProject.model.*;
import com.example.GestionProject.repository.*;
import com.example.GestionProject.service.interfaces.UserStoryInterface;
import jakarta.persistence.EntityNotFoundException;
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
    private final EpicRepository epicRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public UserStoryService(UserStoryRepository userStoryRepository,
                            ProductBacklogRepository productBacklogRepository,
                            SprintBacklogRepository sprintBacklogRepository,
                            EpicRepository epicRepository,
                            TaskRepository taskRepository) {
        this.UserStoryRepository = userStoryRepository;
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

        return convertToDTO(UserStoryRepository.save(us));
    }

    private int calculateNote(UserStory s) {
        return (s.getValeurMetier() * 3)
                + (s.getUrgence() * 2)
                - s.getComplexite()
                - s.getRisques()
                + s.getDependances();
    }

    // Définir la priorité MoSCoW en fonction de la note
    private MoSCoWPriority associatePriorite(int note) {
        if (note >= 18) return MoSCoWPriority.MUST_HAVE;
        else if (note >= 13) return MoSCoWPriority.SHOULD_HAVE;
        else if (note >= 8) return MoSCoWPriority.COULD_HAVE;
        else return MoSCoWPriority.WONT_HAVE;
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByBacklogId(Long backlogId) {
        validateBacklogId(backlogId);
        List<UserStory> userStories = UserStoryRepository.findByProductBacklogId(backlogId);
        return userStories.stream().map(userStory -> convertToDTO(userStory)).collect(Collectors.toList());
    }

    @Override
    public List<UserStoryDTO> getUserStoriesBySprintBacklogId(Long sprintBacklogId) {
        validateSprintBacklogId(sprintBacklogId);
        List<UserStory> userStories = UserStoryRepository.findBySprintBacklogId(sprintBacklogId);
        return userStories.stream().map(userStory -> convertToDTO(userStory)).collect(Collectors.toList());
    }

    @Override
    public UserStoryDTO getUserStoryById(Long id) {
        validateUserStoryId(id);
        UserStory us = UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));
        return convertToDTO(us);
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByEpicId(Long epicId) {
        validateEpicId(epicId);
        List<UserStory> userStories = UserStoryRepository.findByEpicId(epicId);
        return userStories.stream().map(userStory -> convertToDTO(userStory)).collect(Collectors.toList());
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByTaskId(Long taskId) {
        validateTaskId(taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        UserStory userStory = task.getUserStory();

        return List.of(convertToDTO(userStory));
    }

//    @Override
//    public UserStoryDTO addUserStoryToSprintBacklogBySprintId(Long sprintId, Long userStoryId) {
//        validateSprintId(sprintId);
//        validateUserStoryId(userStoryId);
//
//        Sprint sprint = sprintRepository.findById(sprintId)
//                .orElseThrow(()-> new RuntimeException("Sprint introuvable"));
//
//        SprintBacklog sprintBacklog = sprint.getSprintBacklog();
//
//        UserStory userStory = UserStoryRepository.findById(userStoryId)
//                .orElseThrow(()-> new RuntimeException("UserStory introuvable"));
//
//        if(userStory.getSprintBacklog() != null){
//            throw new IllegalArgumentException("Cette User Story est déjà assignée à un Sprint");
//        }
//
//        userStory.setSprintBacklog(sprintBacklog);
//        sprintBacklog.getUserStories().add(userStory);
//
//        UserStory us = UserStoryRepository.save(userStory);
//        sprintBacklogRepository.save(sprintBacklog);
//        return convertToDTO(us);
//    }

    @Override
    public UserStoryDTO addUserStoryToSprintBacklogById(Long sprintBacklogId, Long userStoryId) {
        validateSprintBacklogId(sprintBacklogId);
        validateUserStoryId(userStoryId);

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
        validateEpicId(epicId);
        validateUserStoryId(userStoryId);

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
        validateSprintId(sprintBacklogId);
        validateUserStoryId(userStoryId);

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
        validateUserStoryId(id);

        UserStory us = UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));

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

        return convertToDTO(UserStoryRepository.save(us));
    }

    @Override
    public UserStoryDTO updateUserStoryStatus(Long id, StatutEnum newStatus) {
        validateUserStoryId(id);

        UserStory us = UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));

        us.setStatut(newStatus);

        return convertToDTO(UserStoryRepository.save(us));
    }

    @Override
    public UserStoryDTO updateUserStoryPriority(Long id, MoSCoWPriority newPriority) {
        validateUserStoryId(id);
        UserStory us = UserStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserStory introuvable avec l'ID: " + id));

        us.setPriorite(newPriority);
        return convertToDTO(UserStoryRepository.save(us));
    }

    @Override
    public void deleteUserStoryById(Long id) {
        validateUserStoryId(id);
        if (!UserStoryRepository.existsById(id)) {
            throw new EntityNotFoundException("UserStory non trouvée avec l'ID: " + id);
        }
        UserStoryRepository.deleteById(id);
    }

    ////////////////////////////////////////////////////////
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


    /////////////////////////////////////////
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
                        userStory.getTasks().stream().map(Task::getId).collect(Collectors.toList())
                        : null
                ).build();
    }
}
