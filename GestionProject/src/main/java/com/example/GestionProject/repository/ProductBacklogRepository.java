package com.example.GestionProject.repository;

import com.example.GestionProject.model.ProductBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductBacklogRepository extends JpaRepository<ProductBacklog, Long> {
}
