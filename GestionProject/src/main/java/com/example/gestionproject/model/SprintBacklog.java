package com.example.gestionproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToOne
    @JoinColumn(name = "product_backlog_id", nullable = false)
    private ProductBacklog productBacklog;
}
