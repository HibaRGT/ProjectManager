package com.example.GestionProject.service;

import com.example.GestionProject.model.Epic;
import com.example.GestionProject.repository.EpicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EpicService {

    private final EpicRepository epicRepository;

    @Autowired
    public EpicService(EpicRepository epicRepository) {
        this.epicRepository = epicRepository;
    }

    public List<Epic> getAllEpics() {
        return epicRepository.findAll();
    }

    public Optional<Epic> getEpicById(Long id) {
        return epicRepository.findById(id);
    }

    public Epic saveEpic(Epic epic) {
        return epicRepository.save(epic);
    }

    public void deleteEpic(Long id) {
        epicRepository.deleteById(id);
    }
}


