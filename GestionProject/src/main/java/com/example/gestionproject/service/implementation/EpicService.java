package com.example.gestionproject.service.implementation;

import com.example.gestionproject.dto.EpicDTO;
import com.example.gestionproject.exception.EpicNotFoundException;
import com.example.gestionproject.exception.ProductBacklogNotFound;
import com.example.gestionproject.mapper.EpicMapper;
import com.example.gestionproject.model.Epic;
import com.example.gestionproject.model.ProductBacklog;
import com.example.gestionproject.repository.EpicRepository;
import com.example.gestionproject.repository.ProductBacklogRepository;
import com.example.gestionproject.service.interfaces.EpicInterface;
import com.example.gestionproject.validator.EpicValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EpicService implements EpicInterface {

    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final EpicValidator epicValidator;

    @Autowired
    public EpicService(EpicRepository epicRepository, ProductBacklogRepository productBacklogRepository, EpicValidator epicValidator) {
        this.epicRepository = epicRepository;
        this.productBacklogRepository = productBacklogRepository;
        this.epicValidator = epicValidator;
    }

    @Override
    public EpicDTO createEpic(EpicDTO epicDTO) {
        epicValidator.validate(epicDTO);
        epicValidator.validateId(epicDTO.getProductBacklogId(), "ProductBacklog");

        ProductBacklog productBacklog = getProductBacklogOrThrow(epicDTO.getId());

        Epic epic = Epic.builder()
                        .nom(epicDTO.getNom())
                        .description(epicDTO.getDescription())
                        .productBacklog(productBacklog).build();

        return EpicMapper.toDTO(epicRepository.save(epic));
    }

    @Override
    public EpicDTO getEpicById(Long id) {
        epicValidator.validateId(id, "Epic");
        Epic epic = getEpicOrThrow(id);
        return EpicMapper.toDTO(epic);
    }

    @Override
    public List<EpicDTO> getEpicsByProductBacklogId(Long productBacklogId) {
        epicValidator.validateId(productBacklogId, "ProductBacklog");
        List<Epic> epics = epicRepository.findByProductBacklogId(productBacklogId);

        return epics.stream().map(EpicMapper::toDTO).toList();
    }


    @Override
    public EpicDTO updateEpic(Long id, EpicDTO epicDTO) {
        epicValidator.validateId(id, "Epic");
        epicValidator.validate(epicDTO);

        Epic ep = getEpicOrThrow(id);
        ep.setNom(epicDTO.getNom());
        ep.setDescription(epicDTO.getDescription());

        return EpicMapper.toDTO(epicRepository.save(ep));
    }


    @Override
    public void deleteEpic(Long id) {
        epicValidator.validateId(id, "Epic");
        if (!epicRepository.existsById(id)) {
            throw new EpicNotFoundException("Epic non trouvée avec l'ID: " + id);
        }
        epicRepository.deleteById(id);
    }

    private Epic getEpicOrThrow(Long id) {
        return epicRepository.findById(id)
                .orElseThrow(() -> new EpicNotFoundException("Aucune Epic trouvée avec l'ID: " + id));
    }

    private ProductBacklog getProductBacklogOrThrow(Long id) {
        return productBacklogRepository.findById(id)
                .orElseThrow(() -> new ProductBacklogNotFound("Aucun ProductBacklog trouvé avec l'ID: " + id));
    }


}
