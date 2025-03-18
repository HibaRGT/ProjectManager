package com.example.GestionProject.service.interfaces;

import com.example.GestionProject.dto.EpicDTO;
import com.example.GestionProject.dto.UserStoryDTO;
import com.example.GestionProject.model.Epic;
import com.example.GestionProject.model.UserStory;

import java.util.List;

public interface EpicInterface {
    public EpicDTO createEpic(EpicDTO epicDTO);
    public EpicDTO getEpicById(Long id);
    public List<EpicDTO> getEpicsByProductBacklogId(Long productBacklogId);
    public EpicDTO updateEpic(Long id, EpicDTO epicDTO);
    public void deleteEpic(Long id);
}
