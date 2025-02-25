package com.example.GestionProject.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_story")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"epic", "productBacklog"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_story_id")
    private Long id;

    @Column(name = "titre", nullable = false, length = 100)
    private String titre;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "priorite", nullable = false)
    private int priorite;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutEnum statut;

    @ManyToOne
    @JoinColumn(name = "epic_id")
    private Epic epic;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id")
    private ProductBacklog productBacklog;
}