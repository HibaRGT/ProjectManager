package com.example.GestionProject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long id;

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;



    @OneToOne(mappedBy = "sprint", cascade = CascadeType.ALL)
    private SprintBacklog sprintBacklog;

}
