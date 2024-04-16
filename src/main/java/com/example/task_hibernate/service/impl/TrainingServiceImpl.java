package com.example.task_hibernate.service.impl;

import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.TrainingType;
import com.example.task_hibernate.model.enums.ExerciseType;
import com.example.task_hibernate.repository.TraineeRepository;
import com.example.task_hibernate.repository.TrainerRepository;
import com.example.task_hibernate.repository.TrainingRepository;
import com.example.task_hibernate.service.TrainingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public Optional<Training> getTrainingById(Long id) {
        return trainingRepository.findById(id);
    }

    @Override
    public List<Training> getTraineeTrainingsList(String traineeUsername, Date fromDate, Date toDate, String trainerName, ExerciseType trainingType) {
        return trainingRepository.findByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    @Override
    public List<Training> getTrainerTrainingsList(String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        return trainingRepository.findByTrainerUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }

    @Override
    public Optional<Training> createTraining(Training training) {
        if (training.getTrainingType() == null) {
            log.error("Training type is required");
            return Optional.empty();
        }
        if (training.getTrainingDate() == null) {
            log.error("Training date is required");
            return Optional.empty();
        }
        if (training.getTrainee() == null) {
            log.error("Trainee is required");
            return Optional.empty();
        }
        if (training.getTrainer() == null) {
            log.error("Trainer is required");
            return Optional.empty();
        }
        return Optional.of(trainingRepository.save(training));
    }

    @Override
    public Boolean addTraining(String traineeUserName, String trainerUserName, String trainingName, Date trainingDate, int trainingDuration) {
        Optional<Trainee> trainee = traineeRepository.findByUser_UserName(traineeUserName);
        if (trainee.isEmpty()) {
            log.error("Trainee with username {} not found", traineeUserName);
            return false;
        }
        Optional<Trainer> trainer = trainerRepository.findByUser_UserName(trainerUserName);
        if (trainer.isEmpty()) {
            log.error("Trainer with username {} not found", trainerUserName);
            return false;
        }
        Training training = new Training();
        training.setTrainee(trainee.get());
        training.setTrainer(trainer.get());
        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setDuration(trainingDuration);
        trainingRepository.save(training);
        return true;
    }

    @Override
    public List<Trainer> getTrainersOfTrainee(String traineeUsername) {
        return trainingRepository.findTrainerByTraineeUsername(traineeUsername);
    }

    @Override
    public List<Trainee> getTraineesOfTrainer(String trainerUsername) {
        return trainingRepository.findTraineeByTrainerUsername(trainerUsername);
    }

    @Override
    public Boolean deleteTrainingsWithTrainers(String traineeUsername, List<String> trainerUsernames) {
        return trainingRepository.deleteTraineesTrainingsWithTrainers(traineeUsername, trainerUsernames);
    }

    @Override
    public List<TrainingType> getAllTrainingTypes() {
        return trainingRepository.findAllTrainingTypes();
    }
}
