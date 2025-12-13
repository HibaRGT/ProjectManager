package com.example.gestionproject.repository;

import com.example.gestionproject.model.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpicRepository extends JpaRepository<Epic, Long> {
    List<Epic> findByProductBacklogId(Long productBacklogId);
}
