package com.example.GestionProject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user_story")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"epic", "productBacklog"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_story_id")
    private Long id;

    @Column(name = "titre", nullable = false, length = 100)
    private String titre;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "valeurMetier")
    private int valeurMetier;

    @Column(name = "urgence")
    private int urgence;

    @Column(name = "complexite")
    private int complexite;

    @Column(name = "risques")
    private int risques;

    @Column(name = "dependances")
    private int dependances;

    private int notePriorite;

    @Enumerated(EnumType.STRING)
    @Column(name = "priorite", nullable = false)
    private MoSCoWPriority priorite;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutEnum statut;

    @ManyToOne
    @JoinColumn(name = "epic_id")
    private Epic epic;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id")
    private ProductBacklog productBacklog;

    @ManyToOne
    @JoinColumn(name="sprint_backlog_id")
    private SprintBacklog sprintBacklog;

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

}