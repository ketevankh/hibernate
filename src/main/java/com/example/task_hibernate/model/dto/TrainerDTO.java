package com.example.task_hibernate.model.dto;

import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class TrainerDTO {
    private TrainingType specialization;
    private UserDTO user;
}
