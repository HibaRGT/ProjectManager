package com.example.GestionProject.repository;

import com.example.GestionProject.model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    List<UserStory> findByProductBacklogId(Long backlogId);
    List<UserStory> findByEpicId(Long epicId);
}
