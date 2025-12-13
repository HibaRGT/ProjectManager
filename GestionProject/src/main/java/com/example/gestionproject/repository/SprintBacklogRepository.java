package com.example.gestionproject.repository;

import com.example.gestionproject.model.SprintBacklog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, Long> {
    List<SprintBacklog> findByProductBacklogId(Long productBacklogId);
}
