package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.SprintBacklogDTO;
import com.example.gestionproject.exception.ProductBacklogNotFound;
import com.example.gestionproject.exception.SprintBacklogNotFound;
import com.example.gestionproject.exception.SprintNotFoundException;
import com.example.gestionproject.mapper.SprintBacklogMapper;
import com.example.gestionproject.model.ProductBacklog;
import com.example.gestionproject.model.Sprint;
import com.example.gestionproject.model.SprintBacklog;
import com.example.gestionproject.repository.ProductBacklogRepository;
import com.example.gestionproject.repository.SprintBacklogRepository;
import com.example.gestionproject.repository.SprintRepository;
import com.example.gestionproject.service.interfaces.SprintBacklogInterface;
import com.example.gestionproject.validator.SprintBacklogValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SprintBacklogService implements SprintBacklogInterface{
    private final SprintBacklogRepository sprintBacklogrepository;
    private final SprintRepository sprintRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final SprintBacklogValidator validator;

    @Autowired
    public SprintBacklogService(SprintBacklogRepository sprintBacklogrepository,
                                SprintRepository sprintRepository,
                                ProductBacklogRepository productBacklogRepository, SprintBacklogValidator validator
    ) {
        this.sprintBacklogrepository = sprintBacklogrepository;
        this.sprintRepository = sprintRepository;
        this.productBacklogRepository = productBacklogRepository;
        this.validator = validator;
    }


    @Override
    public SprintBacklogDTO getSprintBacklogById(Long id) {
        validator.validateSprintBacklogId(id);
        SprintBacklog sp = getSprintBacklogOrThrow(id);
        return SprintBacklogMapper.toDTO(sp);
    }

    @Override
    public SprintBacklogDTO getSprintBacklogBySprintId(Long id) {
        validator.validateSprintId(id);
        Sprint sprint = getSprintOrThrow(id);

        SprintBacklog sp = sprint.getSprintBacklog();
        return SprintBacklogMapper.toDTO(sp);

    }

    @Override
    public SprintBacklogDTO createSprintBacklog(SprintBacklogDTO sprintBacklogDTO) {
        validator.validateCreate(sprintBacklogDTO);

        SprintBacklog sprintBacklog = SprintBacklog.builder().build();

        Sprint sprint = getSprintOrThrow(sprintBacklogDTO.getSprintId());
        ProductBacklog pb = getProductBacklogOrThrow(sprintBacklogDTO.getProductBacklogId());

        sprintBacklog.setSprint(sprint);
        sprintBacklog.setProductBacklog(pb);

        SprintBacklog sp = sprintBacklogrepository.save(sprintBacklog);
        return SprintBacklogMapper.toDTO(sp);
    }

    @Override
    public void deleteSprintBacklogById(Long id) {
        validator.validateSprintBacklogId(id);
        SprintBacklog sp = getSprintBacklogOrThrow(id);
        sprintBacklogrepository.delete(sp);
    }

    /* ===== Private helpers (complexity extracted) ===== */

    private SprintBacklog getSprintBacklogOrThrow(Long id) {
        return sprintBacklogrepository.findById(id)
                .orElseThrow(() ->
                        new SprintBacklogNotFound("SprintBacklog introuvable"));
    }

    private Sprint getSprintOrThrow(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow(() ->
                        new SprintNotFoundException("Sprint introuvable"));
    }

    private ProductBacklog getProductBacklogOrThrow(Long id) {
        return productBacklogRepository.findById(id)
                .orElseThrow(() ->
                        new ProductBacklogNotFound("ProductBacklog introuvable"));
    }
}
