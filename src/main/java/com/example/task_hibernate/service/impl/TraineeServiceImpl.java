package com.example.task_hibernate.service.impl;

import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.User;
import com.example.task_hibernate.model.dto.TraineeDTO;
import com.example.task_hibernate.model.dto.UserDTO;
import com.example.task_hibernate.repository.TraineeRepository;
import com.example.task_hibernate.service.TraineeService;
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
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final UserService userService;

    public TraineeServiceImpl(TraineeRepository traineeRepository, UserService userService) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
    }
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
    public Optional<Trainee> updateTrainee(Trainee trainee, Credentials credentials) {
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return Optional.empty();
        }
        Optional<Trainee> tmpTrainee = traineeRepository.findById(trainee.getId());
        if (tmpTrainee.isEmpty()) {
           log.error("Trainee with id {} not found", trainee.getId());
           return Optional.empty();
        }
        UserDTO updateUserDTO = new UserDTO(trainee.getUser().getFirstName(), trainee.getUser().getLastName(), trainee.getUser().getIsActive());
        Optional<User> updatedUser = userService.updateUser(trainee.getUser().getId(), updateUserDTO);
        if (updatedUser.isEmpty()) {
            log.error("User with id {} can't be updated", trainee.getUser().getId());
            return Optional.empty();
        }
        Trainee updateTrainee = tmpTrainee.get();
        updateTrainee.setAddress(trainee.getAddress());
        updateTrainee.setDateOfBirth(trainee.getDateOfBirth());
        updateTrainee.setUser(updatedUser.get());
        traineeRepository.save(updateTrainee);
        return Optional.of(updateTrainee);
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
        if (userService.validateUserFailed(credentials)) {
            log.error("Invalid credentials");
            return false;
        }
        if(!traineeRepository.findByUser_UserName(userName).isPresent()) {
            log.error("Trainee with username {} not found", userName);
            return false;
        }
        traineeRepository.deleteByUser_UserName(userName);
        return true;
    }
}
