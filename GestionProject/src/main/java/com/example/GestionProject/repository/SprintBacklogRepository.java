package com.example.GestionProject.repository;

import com.example.GestionProject.model.Epic;
import com.example.GestionProject.model.SprintBacklog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, Long> {
}
