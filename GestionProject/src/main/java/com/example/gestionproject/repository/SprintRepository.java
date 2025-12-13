package com.example.gestionproject.repository;

import com.example.gestionproject.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findBySprintBacklogId(Long sprintBacklogId);
}
