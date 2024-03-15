package com.example.task_hibernate.service;

import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.dto.TraineeDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TraineeService {
    Trainee createTrainee(TraineeDTO traineeDTO);

    List<Trainee> getAllTrainees(Credentials credentials);

    Optional<Trainee> getTraineeById(Long id, Credentials credentials);

    Optional<Trainee> getTraineeByUserName(String userName, Credentials credentials);

    boolean changeTraineePassword(String password, Credentials credentials);

    Optional<Trainee> updateTrainee(Trainee trainee, Credentials credentials);

    boolean changeActiveStatus(Boolean isActive, Credentials credentials);

    boolean deleteTrainee(String username, Credentials credentials);
}
