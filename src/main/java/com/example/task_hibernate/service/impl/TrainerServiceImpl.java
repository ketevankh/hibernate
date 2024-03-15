package com.example.task_hibernate.service.impl;

import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.User;
import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.model.dto.TrainerDTO;
import com.example.task_hibernate.model.dto.UserDTO;
import com.example.task_hibernate.repository.TrainerRepository;
import com.example.task_hibernate.service.TrainerService;
import com.example.task_hibernate.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserService userService;

    public TrainerServiceImpl(TrainerRepository trainerRepository, UserService userService) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
    }

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
    public Optional<Trainer> updateTrainer(Trainer trainer, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Optional.empty();
        }
        Optional<Trainer> tmpTrainer = trainerRepository.findById(trainer.getId());
        if (tmpTrainer.isEmpty()) {
            log.error("Trainer with id {} not found", trainer.getId());
            return Optional.empty();
        }
        UserDTO updateUserDTO = new UserDTO(trainer.getUser().getFirstName(), trainer.getUser().getLastName(), trainer.getUser().getIsActive());
        Optional<User> updatedUser = userService.updateUser(trainer.getUser().getId(), updateUserDTO);
        if (updatedUser.isEmpty()) {
            log.error("User with id {} can't be updated", trainer.getUser().getId());
            return Optional.empty();
        }
        Trainer updateTrainer = tmpTrainer.get();
        updateTrainer.setTrainingType(updateTrainer.getTrainingType());
        updateTrainer.setUser(updatedUser.get());
        trainerRepository.save(updateTrainer);
        return Optional.of(updateTrainer);
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
}
