package com.example.GestionProject.repository;

import com.example.GestionProject.model.Sprint;
import com.example.GestionProject.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
