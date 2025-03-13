package com.example.GestionProject.service.implementation;

import com.example.GestionProject.model.Epic;
import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.EpicRepository;
import com.example.GestionProject.repository.ProductBacklogRepository;
import com.example.GestionProject.repository.UserStoryRepository;
import com.example.GestionProject.service.interfaces.EpicInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Epic createEpic(Epic epic) {
        validateEpic(epic);
        return epicRepository.save(epic);
    }

    @Override
    public Epic getEpicById(Long id) {
        if (!productBacklogRepository.existsById(id)) {
            throw new RuntimeException("ProductBacklog introuvable avec l'ID: " + id);
        }
        return epicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucune Epic trouvée avec l'ID: " + id));
    }

    @Override
    public List<Epic> getEpicsByProductBacklogId(Long productBacklogId) {
        return epicRepository.findByProductBacklogId(productBacklogId);
    }

    @Override
    public List<Epic> getAllEpics() {
        return epicRepository.findAll();
    }

    @Override
    public Epic updateEpic(Long id, Epic epic) {
        Epic ep = epicRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(("Aucune Epic trouvée avec l'ID: "+ id + " ")));
        ep.setNom(epic.getNom());
        ep.setDescription(epic.getDescription());

        validateEpic(ep);
        return epicRepository.save(ep);

    }

    @Override
    public UserStory addUserStoryToEpic(Long epicId, UserStory userStory) {
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new RuntimeException("Aucune Epic trouvée avec l'ID: " + epicId));

        userStory.setEpic(epic);
        return userStoryRepository.save(userStory);
    }

    @Override
    public List<UserStory> getUserStoriesByEpicId(Long epicId) {
        return userStoryRepository.findByEpicId(epicId);
    }

    @Override
    public void deleteEpic(Long id) {
        if (!epicRepository.existsById(id)) {
            throw new RuntimeException("Epic non trouvée avec l'ID: " + id);
        }
        epicRepository.deleteById(id);
    }

    private void validateEpic(Epic epic) {
        if (epic.getNom() == null || epic.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le titre de l'epic ne peut pas être vide");
        }
        if (epic.getDescription() == null || epic.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La description de l'epic ne peut pas être vide");
        }
    }


    }
