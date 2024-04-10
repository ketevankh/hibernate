package com.example.task_hibernate.model;

import com.example.task_hibernate.model.enums.TrainingTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TRAINING_TYPES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingType {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private TrainingTypeEnum trainingType;
}
