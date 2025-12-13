package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.EpicDTO;
import com.example.gestionproject.model.Epic;
import com.example.gestionproject.model.ProductBacklog;
import com.example.gestionproject.model.UserStory;
import com.example.gestionproject.repository.EpicRepository;
import com.example.gestionproject.repository.ProductBacklogRepository;
import com.example.gestionproject.service.interfaces.EpicInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EpicService implements EpicInterface {

    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;

    @Autowired
    public EpicService(EpicRepository epicRepository, ProductBacklogRepository productBacklogRepository) {
        this.epicRepository = epicRepository;
        this.productBacklogRepository = productBacklogRepository;
    }

    @Override
    public EpicDTO createEpic(EpicDTO epicDTO) {
        validateEpic(epicDTO);

        if (epicDTO.getProductBacklogId() == null) {
            throw new IllegalArgumentException("L'ID du ProductBacklog ne peut pas être nul");
        }

        ProductBacklog productBacklog = productBacklogRepository.findById(epicDTO.getProductBacklogId())
                .orElseThrow(() -> new RuntimeException("Aucun ProductBacklog trouvé avec l'ID: " + epicDTO.getProductBacklogId()));


        Epic epic = Epic.builder()
                        .nom(epicDTO.getNom())
                        .description(epicDTO.getDescription())
                        .productBacklog(productBacklog).build();

        return convertToDTO(epicRepository.save(epic));
    }

    @Override
    public EpicDTO getEpicById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de l'epic ne peut pas être null");
        }

        Epic epic = epicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucune Epic trouvée avec l'ID: " + id));
        return convertToDTO(epic);
    }

    @Override
    public List<EpicDTO> getEpicsByProductBacklogId(Long productBacklogId) {
        if (productBacklogId == null) {
            throw new IllegalArgumentException("L'ID du produit backlog ne peut pas être null");
        }
        List<Epic> epics = epicRepository.findByProductBacklogId(productBacklogId);

        return epics.stream().map(this::convertToDTO).toList();
    }


    @Override
    public EpicDTO updateEpic(Long id, EpicDTO epicDTO) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de l'epic ne peut pas être null");
        }

        Epic ep = epicRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(("Aucune Epic trouvée avec l'ID: "+ id)));

        validateEpic(epicDTO);

        ep.setNom(epicDTO.getNom());
        ep.setDescription(epicDTO.getDescription());

        return convertToDTO(epicRepository.save(ep));

    }


    @Override
    public void deleteEpic(Long id) {
        if(id == null){
            throw new IllegalArgumentException("L'ID de l'epic ne peut pas être null");
        }

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
        return EpicDTO.builder()
                .id(epic.getId())
                .nom(epic.getNom())
                .description(epic.getDescription())
                .productBacklogId(epic.getProductBacklog() != null
                        ? epic.getProductBacklog().getId()
                        : null)
                .userStoryIds(epic.getUserStories() != null
                        ? epic.getUserStories().stream().map(UserStory::getId).collect(Collectors.toList())
                        : null)
                .build();
    }


    }
