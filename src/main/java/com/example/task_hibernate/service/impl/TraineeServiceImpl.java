package com.example.task_hibernate.service.impl;

import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.User;
import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.model.dto.serviceDTOs.TraineeDTO;
import com.example.task_hibernate.model.enums.ExerciseType;
import com.example.task_hibernate.repository.TraineeRepository;
import com.example.task_hibernate.service.TraineeService;
import com.example.task_hibernate.service.TrainerService;
import com.example.task_hibernate.service.TrainingService;
import com.example.task_hibernate.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final TrainingService trainingService;

    private final TrainerService trainerService;

    @Override
    public Trainee createTrainee(TraineeDTO traineeDTO) {
        User user = userService.createUser(traineeDTO.getUser());
        Trainee trainee = new Trainee();
        trainee.setAddress(traineeDTO.getAddress());
        trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
        trainee.setUser(user);
        return traineeRepository.save(trainee);
    }

    @Override
    public List<Trainee> getAllTrainees(Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Collections.emptyList();
        }
        return traineeRepository.findAll();
    }

    @Override
    public Optional<Trainee> getTraineeById(Long id, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Optional.empty();
        }
        return traineeRepository.findById(id);
    }

    @Override
    public Optional<Trainee> getTraineeByUserName(String userName, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Optional.empty();
        }
        return traineeRepository.findByUser_UserName(userName);
    }

    @Override
    public boolean changeTraineePassword(String password, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return false;
        }
        return userService.changeUserPassword(credentials.userName(), password);
    }

    @Override
    public Optional<Trainee> updateTrainee(TraineeDTO trainee, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Optional.empty();
        }
        Trainee tmpTrainee = traineeRepository.findByUser_UserName(credentials.userName()).get();

        Optional<User> updatedUser = userService.updateUser(tmpTrainee.getUser().getId(), trainee.getUser());
        if (updatedUser.isEmpty()) {
            log.error("user update failed");
            return Optional.empty();
        }

        tmpTrainee.setAddress(trainee.getAddress());
        tmpTrainee.setDateOfBirth(trainee.getDateOfBirth());
        tmpTrainee.setUser(updatedUser.get());
        return Optional.of(traineeRepository.save(tmpTrainee));
    }

    @Override
    public boolean changeActiveStatus(Boolean isActive, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return false;
        }
        Trainee trainee = traineeRepository.findByUser_UserName(credentials.userName()).get();
        return userService.changeActiveStatus(trainee.getUser().getId(), isActive);
    }

    @Override
    public boolean deleteTrainee(String userName, Credentials credentials) {

        //FIXME Best practice is to have only one return statement
        boolean traineeDeleted = false;

        if (userService.validateUserFailed(credentials)) { //FIXME 'validateUserFailed' would be better to move to Validator which will be called from controller and all typical actions like 'log.error("Invalid credentials")' must be a part of validator method implementation
            log.error("Invalid credentials");
        } else if (traineeRepository.findByUser_UserName(userName).isEmpty()) {
            log.error("Trainee with username {} not found", userName);
        } else {
            traineeRepository.deleteByUser_UserName(userName);
            traineeDeleted = true;
        }
        return traineeDeleted;
    }

    @Override
    public List<Trainer> getTrainers(String userName, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Collections.emptyList();
        }
        Optional<Trainee> trainee = traineeRepository.findByUser_UserName(userName);
        if (!trainee.isPresent()) {
            log.error("Trainee with username {} not found, userName)", userName);
            return Collections.emptyList();
        }
        return trainingService.getTrainersOfTrainee(userName);
    }

    @Override
    public List<Trainer> getActiveTrainersNotAssignedTo(String userName, Credentials credentials) {
        List<Trainer> allTrainers = trainerService.getAllTrainers(credentials);
        if(allTrainers.isEmpty()) {
            log.error("Invalid credentials");
            return Collections.emptyList();
        }
        List<Trainer> asssignedTrainers = getTrainers(userName, credentials);
        if (!asssignedTrainers.isEmpty()) {
            for (Trainer trainer : asssignedTrainers) {
                if (trainer.getUser().getIsActive()) {
                    allTrainers.remove(trainer);
                }
            }
        }
        return allTrainers;
    }

    @Override
    public List<Trainer> updateTrainersList(String userName, List<String> trainerUsernames, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Collections.emptyList();
        }
        Optional<Trainee> trainee = traineeRepository.findByUser_UserName(userName);
        if (!trainee.isPresent()) {
            log.error("Trainee with username {} not found", userName);
            return Collections.emptyList();
        }
        List<Trainer> tmpTrainers = trainingService.getTrainersOfTrainee(userName);
        List<String> toRemove = new ArrayList<>();
        if (tmpTrainers.isEmpty()) {
            log.info("No Trainings with Trainers found, add training first");
            return Collections.emptyList();
        }
        if (trainerUsernames.isEmpty()) {
            log.info("All trainers and trainings are deleted for user: {}", userName);
            toRemove = tmpTrainers.stream().map(trainer -> trainer.getUser().getUserName()).toList();
        } else {
            for (Trainer trainer : tmpTrainers) {
                if (!trainerUsernames.contains(trainer.getUser().getUserName())) {
                    toRemove.add(trainer.getUser().getUserName());
                } else {
                    log.info("No Trainings with Trainer: {} found, add training first", trainer.getUser().getUserName());
                }
            }
        }
        if (toRemove.isEmpty()) {
            log.info("No updates with TrainersList for User: {}", userName);
        } else {
            trainingService.deleteTrainingsWithTrainers(userName, toRemove);
        }
        return trainingService.getTrainersOfTrainee(userName);
    }

    @Override
    public List<Training> getTrainings(String userName, Date from, Date to, String trainerUserName, String trainingType, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Collections.emptyList(); //FIXME Returning the immutable sheet is not always justified
        }
        Optional<Trainee> trainee = traineeRepository.findByUser_UserName(userName);
        if (trainee.isEmpty()) {
            log.error("Trainee with username {} not found", userName); //FIXME usually we have error logs only when we catch or throw some exceptions, here WARN or INFO would be better
            return Collections.emptyList();
        }
        return trainingService.getTraineeTrainingsList(userName, from, to, trainerUserName, ExerciseType.valueOf(trainingType));
    }

}
