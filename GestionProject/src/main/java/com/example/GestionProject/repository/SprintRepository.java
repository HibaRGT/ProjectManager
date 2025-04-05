package com.example.GestionProject.repository;

import com.example.GestionProject.dto.SprintDTO;
import com.example.GestionProject.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findBySprintBacklogId(Long sprintBacklogId);
}
