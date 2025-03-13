package com.example.GestionProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SprintBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_backlog_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    @OneToMany(mappedBy = "sprintBacklog")
    private List<UserStory> userStories = new ArrayList<>();
}
