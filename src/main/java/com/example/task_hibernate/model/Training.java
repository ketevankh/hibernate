package com.example.task_hibernate.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "TRAININGS")
@Data
@NoArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Trainee trainee;

    @ManyToOne
    private Trainer trainer;

    @ManyToOne
    private TrainingType trainingType;

    @Column(nullable = false)
    private String trainingName;

    @Column(name = "training_date", nullable = false)
    private Date trainingDate;

    @Column(nullable = false)
    private int duration;
}
