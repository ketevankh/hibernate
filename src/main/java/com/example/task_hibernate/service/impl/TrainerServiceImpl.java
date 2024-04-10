package com.example.task_hibernate.service.impl;

import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.User;
import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.model.dto.serviceDTOs.TrainerDTO;
import com.example.task_hibernate.model.dto.serviceDTOs.UserDTO;
import com.example.task_hibernate.model.enums.TrainingTypeEnum;
import com.example.task_hibernate.repository.TrainerRepository;
import com.example.task_hibernate.service.TrainerService;
import com.example.task_hibernate.service.TrainingService;
import com.example.task_hibernate.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final TrainingService trainingService;

    public Trainer createTrainer(TrainerDTO trainerDTO) {
        User user = userService.createUser(trainerDTO.getUser());
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(trainerDTO.getSpecialization());
        return trainerRepository.save(trainer);
    }

    @Override
    public List<Trainer> getAllTrainers(Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Collections.emptyList();
        }
        return trainerRepository.findAll();
    }

    @Override
    public Optional<Trainer> getTrainerById(Long id, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Optional.empty();
        }
        return trainerRepository.findById(id);
    }

    @Override
    public Optional<Trainer> getTrainerByUserName(String userName, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Optional.empty();
        }
        return trainerRepository.findByUser_UserName(userName);
    }
    @Override
    public boolean changeTrainerPassword(String password, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return false;
        }
        return userService.changeUserPassword(password, credentials.userName());
    }

    @Override
    public Optional<Trainer> updateTrainer(TrainerDTO trainer, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Optional.empty();
        }
        Trainer tmpTrainer = trainerRepository.findByUser_UserName(credentials.userName()).get();

        Optional<User> updatedUser = userService.updateUser(tmpTrainer.getUser().getId(), trainer.getUser());
        if (updatedUser.isEmpty()) {
            log.error("user update failed");
            return Optional.empty();
        }
        tmpTrainer.setTrainingType(trainer.getSpecialization());
        tmpTrainer.setUser(updatedUser.get());
        return Optional.of(trainerRepository.save(tmpTrainer));
    }
    @Override
    public boolean changeActiveStatus(Boolean isActive, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return false;
        }
        Trainer trainer = trainerRepository.findByUser_UserName(credentials.userName()).get();
        return userService.changeActiveStatus(trainer.getUser().getId(), isActive);
    }

    @Override
    public boolean deleteTrainer(String userName, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return false;
        }
        if(!trainerRepository.findByUser_UserName(userName).isPresent()) {
            log.error("Trainee with username {} not found", userName);
            return false;
        }
        trainerRepository.deleteByUser_UserName(userName);
        return true;
    }

    @Override
    public List<Trainee> getTrainees(String userName, Credentials credentials) {
        if(userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Collections.emptyList();
        }
        Optional<Trainer> trainer = trainerRepository.findByUser_UserName(userName);
        if(!trainer.isPresent()) {
            log.error("Trainer with username {} not found, userName)", userName);
            return Collections.emptyList();
        }
        return trainingService.getTraineesOfTrainer(userName);
    }

    @Override
    public List<Training> getTrainings(String userName, Date from, Date to, String trainerUserName, String trainingType, Credentials credentials) {
        if(userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Collections.emptyList();
        }
        Optional<Trainer> trainer = trainerRepository.findByUser_UserName(userName);
        if(!trainer.isPresent()) {
            log.error("Trainee with username {} not found", userName);
            return Collections.emptyList();
        }
        return trainingService.getTrainerTrainingsList(userName, from, to, trainerUserName);
    }
}
