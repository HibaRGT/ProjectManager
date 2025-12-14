package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.UserStoryDTO;
import com.example.gestionproject.exception.ProductBacklogNotFound;
import com.example.gestionproject.exception.TaskNotFoundException;
import com.example.gestionproject.exception.UserStoryNotFoundException;
import com.example.gestionproject.mapper.UserStoryMapper;
import com.example.gestionproject.model.*;
import com.example.gestionproject.repository.*;
import com.example.gestionproject.service.implementation.helpers.PriorityCalculator;
import com.example.gestionproject.service.interfaces.UserStoryInterface;
import com.example.gestionproject.validator.UserStoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserStoryService implements UserStoryInterface {
    private final UserStoryRepository userStoryRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final SprintBacklogRepository sprintBacklogRepository;
    private final EpicRepository epicRepository;
    private final TaskRepository taskRepository;
    private final PriorityCalculator priorityCalculator;
    private final UserStoryValidator userStoryValidator;

    private static final String NOT_FOUND_PROJECT = "UserStory introuvable avec l'ID: ";

    @Autowired
    public UserStoryService(UserStoryRepository userStoryRepository,
                            ProductBacklogRepository productBacklogRepository,
                            SprintBacklogRepository sprintBacklogRepository,
                            EpicRepository epicRepository,
                            TaskRepository taskRepository,
                            PriorityCalculator priorityCalculator, UserStoryValidator userStoryValidator) {
        this.userStoryRepository = userStoryRepository;
        this.productBacklogRepository = productBacklogRepository;
        this.sprintBacklogRepository = sprintBacklogRepository;
        this.epicRepository = epicRepository;
        this.taskRepository = taskRepository;
        this.priorityCalculator = priorityCalculator;
        this.userStoryValidator = userStoryValidator;
    }

    @Override
    public UserStoryDTO createUserStoryInBacklog(UserStoryDTO userStoryDTO, Long backlogId) {
        userStoryValidator.validateBacklogId(backlogId);
        ProductBacklog backlog = findProductBacklogById(backlogId);
        userStoryValidator.validateUserStory(userStoryDTO);

        UserStory us = UserStoryMapper.toEntity(userStoryDTO, backlog);
        priorityCalculator.calculateAndSetPriority(us);

        return UserStoryMapper.toDTO(userStoryRepository.save(us));
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByBacklogId(Long backlogId) {
        userStoryValidator.validateBacklogId(backlogId);
        return UserStoryMapper.toDTOList(userStoryRepository.findByProductBacklogId(backlogId));
    }

    @Override
    public List<UserStoryDTO> getUserStoriesBySprintBacklogId(Long sprintBacklogId) {
        userStoryValidator.validateSprintBacklogId(sprintBacklogId);
        return UserStoryMapper.toDTOList(userStoryRepository.findBySprintBacklogId(sprintBacklogId));
    }

    @Override
    public UserStoryDTO getUserStoryById(Long id) {
        userStoryValidator.validateUserStoryId(id);
        return UserStoryMapper.toDTO(findUserStoryById(id));
    }

    @Override
    public List<UserStoryDTO> getUserStoriesByEpicId(Long epicId) {
        userStoryValidator.validateEpicId(epicId);
        return UserStoryMapper.toDTOList(userStoryRepository.findByEpicId(epicId));
    }

    @Override
    public UserStoryDTO getUserStoryByTaskId(Long taskId) {
        userStoryValidator.validateTaskId(taskId);
        Task task = findTaskById(taskId);
        return UserStoryMapper.toDTO(task.getUserStory());
    }

    @Override
    public UserStoryDTO addUserStoryToSprintBacklogById(Long sprintBacklogId, Long userStoryId) {
        userStoryValidator.validateSprintBacklogId(sprintBacklogId);
        userStoryValidator.validateUserStoryId(userStoryId);

        SprintBacklog sprintBacklog = findSprintBacklogById(sprintBacklogId);
        UserStory userStory = findUserStoryById(userStoryId);

        userStoryValidator.validateUserStoryNotAssigned(userStory);
        assignUserStoryToSprint(userStory, sprintBacklog);

        return UserStoryMapper.toDTO(userStoryRepository.save(userStory));
    }

    private void assignUserStoryToSprint(UserStory userStory, SprintBacklog sprintBacklog) {
        userStory.setSprintBacklog(sprintBacklog);
        sprintBacklog.getUserStories().add(userStory);
        sprintBacklogRepository.save(sprintBacklog);
    }

    @Override
    public UserStoryDTO addUserStoryToEpic(Long epicId, Long userStoryId) {
        userStoryValidator.validateEpicId(epicId);
        userStoryValidator.validateUserStoryId(userStoryId);

        Epic epic = findEpicById(epicId);
        UserStory us = findUserStoryById(userStoryId);

        assignUserStoryToEpic(us, epic);

        return UserStoryMapper.toDTO(us);
    }

    private void assignUserStoryToEpic(UserStory us, Epic epic) {
        us.setEpic(epic);
        initializeEpicUserStoriesIfNull(epic);
        epic.getUserStories().add(us);
        userStoryRepository.save(us);
        epicRepository.save(epic);
    }

    private void initializeEpicUserStoriesIfNull(Epic epic) {
        if (epic.getUserStories() == null) {
            epic.setUserStories(new ArrayList<>());
        }
    }

    @Override
    public UserStoryDTO removeUserStoryFromSprintBacklog(Long sprintBacklogId, Long userStoryId) {
        userStoryValidator.validateSprintBacklogId(sprintBacklogId);
        userStoryValidator.validateUserStoryId(userStoryId);

        SprintBacklog sprintBacklog = findSprintBacklogById(sprintBacklogId);
        UserStory userStory = findUserStoryById(userStoryId);

        userStoryValidator.validateUserStoryAssignedToSprint(userStory, sprintBacklog);
        unassignUserStoryFromSprint(userStory, sprintBacklog);

        return UserStoryMapper.toDTO(userStoryRepository.save(userStory));
    }

    private void unassignUserStoryFromSprint(UserStory userStory, SprintBacklog sprintBacklog) {
        userStory.setSprintBacklog(null);
        sprintBacklog.getUserStories().remove(userStory);
        sprintBacklogRepository.save(sprintBacklog);
    }

    @Override
    public UserStoryDTO updateUserStory(Long id, UserStoryDTO userStoryDetails) {
        userStoryValidator.validateUserStoryId(id);
        UserStory us = findUserStoryById(id);
        userStoryValidator.validateUserStory(userStoryDetails);

        updateUserStoryFields(us, userStoryDetails);

        return UserStoryMapper.toDTO(userStoryRepository.save(us));
    }

    private void updateUserStoryFields(UserStory us, UserStoryDTO details) {
        us.setTitre(details.getTitre());
        us.setDescription(details.getDescription());
        us.setValeurMetier(details.getValeurMetier());
        us.setUrgence(details.getUrgence());
        us.setComplexite(details.getComplexite());
        us.setRisques(details.getRisques());
        us.setDependances(details.getDependances());

        if (details.getStatut() != null) {
            us.setStatut(details.getStatut());
        }

        priorityCalculator.calculateAndSetPriority(us);
    }

    @Override
    public UserStoryDTO updateUserStoryStatus(Long id, StatutEnum newStatus) {
        userStoryValidator.validateUserStoryId(id);
        UserStory us = findUserStoryById(id);
        us.setStatut(newStatus);
        return UserStoryMapper.toDTO(userStoryRepository.save(us));
    }

    @Override
    public UserStoryDTO updateUserStoryPriority(Long id, MoSCoWPriority newPriority) {
        userStoryValidator.validateUserStoryId(id);
        UserStory us = findUserStoryById(id);
        us.setPriorite(newPriority);
        return UserStoryMapper.toDTO(userStoryRepository.save(us));
    }

    @Override
    public void deleteUserStoryById(Long id) {
        userStoryValidator.validateUserStoryId(id);
        validateUserStoryExists(id);
        userStoryRepository.deleteById(id);
    }

    // Finder methods - centralized repository calls
    private UserStory findUserStoryById(Long id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_PROJECT + id));
    }

    private SprintBacklog findSprintBacklogById(Long id) {
        return sprintBacklogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SprintBacklog introuvable"));
    }

    private Epic findEpicById(Long id) {
        return epicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucune Epic trouvée avec l'ID: " + id));
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }

    private ProductBacklog findProductBacklogById(Long id){
        return productBacklogRepository.findById(id)
                .orElseThrow(() -> new ProductBacklogNotFound("ProductBacklog non trouvé avec l'ID: " + id));

    }

    public void validateUserStoryExists(Long id) {
        if (!userStoryRepository.existsById(id)) {
            throw new UserStoryNotFoundException("UserStory non trouvée avec l'ID: " + id);
        }
    }
}