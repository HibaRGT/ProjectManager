package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.SprintDTO;
import com.example.gestionproject.exception.SprintNotFoundException;
import com.example.gestionproject.mapper.SprintMapper;
import com.example.gestionproject.model.Sprint;
import com.example.gestionproject.model.SprintBacklog;
import com.example.gestionproject.repository.SprintBacklogRepository;
import com.example.gestionproject.repository.SprintRepository;
import com.example.gestionproject.service.interfaces.SprintInterface;
import com.example.gestionproject.validator.SprintValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SprintService implements SprintInterface {
    private final SprintRepository sprintRepository;
    private final SprintBacklogRepository sprintBacklogRepository;
    private final SprintValidator sprintValidator;

    @Autowired
    public SprintService(SprintRepository sprintRepository
    , SprintBacklogRepository sprintBacklogRepository, SprintValidator sprintValidator) {
        this.sprintRepository = sprintRepository;
        this.sprintBacklogRepository = sprintBacklogRepository;
        this.sprintValidator = sprintValidator;
    }

    @Override
    public SprintDTO createSprint(SprintDTO sprintDTO) {
        sprintValidator.validateSprintData(sprintDTO);

        Sprint sprint = SprintMapper.toEntity(sprintDTO);
        Sprint savedSprint = sprintRepository.save(sprint);
        return SprintMapper.toDTO((savedSprint));
    }

    @Override
    public SprintDTO getSprintById(Long id) {
        sprintValidator.validateSprintId(id);
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new SprintNotFoundException("Sprint non trouvé avec l'ID: " + id));
        return SprintMapper.toDTO(sprint);
    }

    @Override
    public SprintDTO updateSprint(Long id, SprintDTO sprintDetails) {
        sprintValidator.validateSprintId(id);
        Sprint existingSprint = sprintRepository.findById(id)
                .orElseThrow(() -> new SprintNotFoundException("Sprint non trouvé avec l'ID: " + id));

        sprintValidator.validateSprintData(sprintDetails);

        existingSprint.setName(sprintDetails.getName());
        existingSprint.setDescription(sprintDetails.getDescription());
        existingSprint.setStartDate(sprintDetails.getStartDate());
        existingSprint.setEndDate(sprintDetails.getEndDate());

        return SprintMapper.toDTO(sprintRepository.save(existingSprint));
    }

    public List<SprintDTO> getSprintsByProductBacklogId(Long productBacklogId) {
        List<SprintBacklog> sprintBacklogs = sprintBacklogRepository.findByProductBacklogId(productBacklogId);
        return sprintBacklogs.stream()
                .map(SprintBacklog::getSprint)
                .map(SprintMapper::toDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void deleteSprint(Long id) {
        sprintValidator.validateSprintId(id);

        if(!sprintRepository.existsById(id)) {
            throw new SprintNotFoundException("Sprint non trouvée avec l'ID: "+ id);
        }
        sprintRepository.deleteById(id);
    }

}
