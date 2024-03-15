package com.example.task_hibernate.service;

import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.enums.TrainingTypeEnum;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    List<Training> getAllTrainings();

    Optional<Training> getTrainingById(Long id);

    List<Training> getTraineeTrainingsList(String traineeUsername, Date fromDate, Date toDate, String trainerName, TrainingTypeEnum trainingType);

    List<Training> getTrainerTrainingsList(String trainerUsername, Date fromDate, Date toDate, String traineeName);

    Optional<Training> createTraining(Training training);

    List<Trainer> getTrainersOfTrainee(String traineeUsername);
}
