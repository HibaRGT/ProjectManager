package com.example.GestionProject.repository;

import com.example.GestionProject.model.Role;
import com.example.GestionProject.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
