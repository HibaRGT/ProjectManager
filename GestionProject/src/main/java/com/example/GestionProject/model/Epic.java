package com.example.GestionProject.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Epic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "epic_id")
    private Long id;
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;
    @Column(name = "description", length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id")
    private ProductBacklog productBacklog;

    @OneToMany(mappedBy = "epic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStory> userStories;
}
