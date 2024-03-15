package com.example.task_hibernate.service.impl;

import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.enums.TrainingTypeEnum;
import com.example.task_hibernate.repository.TrainingRepository;
import com.example.task_hibernate.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }
    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Optional<Training> getTrainingById(Long id) {
        return trainingRepository.findById(id);
    }

    public List<Training> getTraineeTrainingsList(String traineeUsername, Date fromDate, Date toDate, String trainerName, TrainingTypeEnum trainingType) {
        return trainingRepository.findByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }
    public List<Training> getTrainerTrainingsList(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        return trainingRepository.findByTrainerUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }

    public Optional<Training> createTraining(Training training) {
        if(training.getTrainingType() == null) {
            log.error("Training type is required");
            return Optional.empty();
        }
        if(training.getTrainingDate() == null) {
            log.error("Training date is required");
            return Optional.empty();
        }
        if(training.getTrainee() == null) {
            log.error("Trainee is required");
            return Optional.empty();
        }
        if(training.getTrainer() == null) {
            log.error("Trainer is required");
            return Optional.empty();
        }
        return Optional.of(trainingRepository.save(training));
    }
    public List<Trainer> getTrainersOfTrainee(String traineeUsername) {
        return trainingRepository.findTrainerByTraineeUsername(traineeUsername);
    }
}
