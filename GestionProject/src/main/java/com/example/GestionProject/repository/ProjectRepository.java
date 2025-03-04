package com.example.GestionProject.repository;

import com.example.GestionProject.model.ProductBacklog;
import com.example.GestionProject.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
