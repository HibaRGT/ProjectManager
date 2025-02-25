package com.example.GestionProject.service;

import com.example.GestionProject.model.UserStory;
import com.example.GestionProject.repository.UserStoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;

    public UserStoryService(UserStoryRepository userStoryRepository) {
        this.userStoryRepository = userStoryRepository;
    }

    public List<UserStory> getAllUserStories() {
        return (List<UserStory>) userStoryRepository.findAll();
    }

    public UserStory addUserStory(UserStory userStory) {
        return userStoryRepository.save(userStory);
    }

    public void deleteUserStory(Long id) {
        userStoryRepository.deleteById(id);
    }
}
