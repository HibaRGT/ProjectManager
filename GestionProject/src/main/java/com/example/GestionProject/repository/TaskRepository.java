package com.example.GestionProject.repository;

import com.example.GestionProject.model.Sprint;
import com.example.GestionProject.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserStoryId(Long userStoryId);
}
