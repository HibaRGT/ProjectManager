package com.example.gestionproject.service.interfaces;

import com.example.gestionproject.dto.EpicDTO;

import java.util.List;

public interface EpicInterface {
    public EpicDTO createEpic(EpicDTO epicDTO);
    public EpicDTO getEpicById(Long id);
    public List<EpicDTO> getEpicsByProductBacklogId(Long productBacklogId);
    public EpicDTO updateEpic(Long id, EpicDTO epicDTO);
    public void deleteEpic(Long id);
}
