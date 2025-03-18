package com.example.GestionProject.service.implementation;

import com.example.GestionProject.dto.EpicDTO;
import com.example.GestionProject.dto.UserStoryDTO;
import com.example.GestionProject.model.Epic;
import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.Task;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.EpicRepository;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.interfaces.EpicInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpicService implements EpicInterface {

    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final UserStoryRepository userStoryRepository;

    @Autowired
    public EpicService(EpicRepository epicRepository, ProductBacklogRepository productBacklogRepository, UserStoryRepository userStoryRepository) {
        this.epicRepository = epicRepository;
        this.productBacklogRepository = productBacklogRepository;
        this.userStoryRepository = userStoryRepository;
    }

    @Override
    public EpicDTO createEpic(EpicDTO epicDTO) {
        validateEpic(epicDTO);

        if (epicDTO.getProductBacklogId() == null) {
            throw new IllegalArgumentException("L'ID du ProductBacklog ne peut pas être nul");
        }

        ProductBacklog productBacklog = productBacklogRepository.findById(epicDTO.getProductBacklogId())
                .orElseThrow(() -> new RuntimeException("Aucun ProductBacklog trouvé avec l'ID: " + epicDTO.getProductBacklogId()));


        Epic epic = new Epic();
        epic.setNom(epicDTO.getNom());
        epic.setDescription(epicDTO.getDescription());
        epic.setProductBacklog(productBacklog);

        return convertToDTO(epicRepository.save(epic));
    }

    @Override
    public EpicDTO getEpicById(Long id) {

        Epic epic = epicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucune Epic trouvée avec l'ID: " + id));
        return convertToDTO(epic);
    }

    @Override
    public List<EpicDTO> getEpicsByProductBacklogId(Long productBacklogId) {
        List<Epic> epics = epicRepository.findByProductBacklogId(productBacklogId);
        return epics.stream().map(epic -> convertToDTO(epic)).collect(Collectors.toList());
    }


    @Override
    public EpicDTO updateEpic(Long id, EpicDTO epicDTO) {
        Epic ep = epicRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(("Aucune Epic trouvée avec l'ID: "+ id + " ")));

        validateEpic(epicDTO);

        ep.setNom(epicDTO.getNom());
        ep.setDescription(epicDTO.getDescription());

        return convertToDTO(epicRepository.save(ep));

    }

    @Override
    public UserStoryDTO addUserStoryToEpic(Long epicId, UserStoryDTO userStoryDTO) {
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new RuntimeException("Aucune Epic trouvée avec l'ID: " + epicId));


        UserStory us = convertToEntity(userStoryDTO);

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
    public void deleteEpic(Long id) {
        if (!epicRepository.existsById(id)) {
            throw new RuntimeException("Epic non trouvée avec l'ID: " + id);
        }
        epicRepository.deleteById(id);
    }

    private void validateEpic(EpicDTO epicDTO) {
        if (epicDTO.getNom() == null || epicDTO.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le titre de l'epic ne peut pas être vide");
        }
        if (epicDTO.getDescription() == null || epicDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de l'epic ne peut pas être vide");
        }
    }

    public EpicDTO convertToDTO(Epic epic) {
        return new EpicDTO(
          epic.getId(),
          epic.getNom(),
          epic.getDescription(),
          epic.getProductBacklog() != null ? epic.getProductBacklog().getId() : null,
          epic.getUserStories() != null ?
                  epic.getUserStories().stream().map(UserStory::getId).collect(Collectors.toList())
                  : null
        );
    }

    private UserStory convertToEntity(UserStoryDTO userStoryDTO) {
        UserStory userStory = new UserStory();
        userStory.setId(userStoryDTO.getId());
        userStory.setTitre(userStoryDTO.getTitre());
        userStory.setDescription(userStoryDTO.getDescription());
        userStory.setPriorite(userStoryDTO.getPriorite());
        userStory.setStatut(userStoryDTO.getStatut());
        return userStory;
    }

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
