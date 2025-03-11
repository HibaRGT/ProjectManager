package com.example.GestionProject.service;

import com.example.GestionProject.model.Epic;
import com.example.GestionProject.model.UserStory;

import java.util.List;

public interface EpicInterface {
    public Epic createEpic(Epic epic);
    public Epic getEpicById(Long id);
    public List<Epic> getEpicsByProductBacklogId(Long productBacklogId);
    public List<Epic> getAllEpics();
    public Epic updateEpic(Long id, Epic epic);
    public UserStory addUserStoryToEpic(Long epicId, UserStory userStory);
    public List<UserStory> getUserStoriesByEpicId(Long epicId);
}
