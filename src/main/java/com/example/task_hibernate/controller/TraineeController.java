package com.example.task_hibernate.controller;

import com.example.task_hibernate.mapper.TraineeMapper;
import com.example.task_hibernate.mapper.TrainerMapper;
import com.example.task_hibernate.mapper.TrainingMapper;
import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TrainingTraineeResponseDTO;
import com.example.task_hibernate.model.dto.serviceDTOs.TraineeDTO;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TraineeResponseDTO;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TrainerDTOForList;
import com.example.task_hibernate.model.dto.controllerDTOs.request.TraineeRequestDTO;
import com.example.task_hibernate.service.TraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;

    @PostMapping(path = "/register")
    public ResponseEntity<Credentials> registerTrainee(@RequestBody final TraineeRequestDTO traineeRequestDTO) {
        TraineeDTO traineeDTO = traineeMapper.traineeRequestDTOToTraineeDTO(traineeRequestDTO);
        Trainee trainee = traineeService.createTrainee(traineeDTO);
        Credentials credentials = new Credentials(trainee.getUser().getUserName(), trainee.getUser().getPassword());
        return ResponseEntity.ok(credentials);
    }
    @GetMapping(path = "/{userNameToFind}")
    public ResponseEntity<TraineeResponseDTO> getTraineeByUseName(@PathVariable final String userNameToFind, @RequestHeader final String userName, @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        Trainee trainee = traineeService.getTraineeByUserName(userNameToFind, credentials).orElse(null);
        if (trainee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Trainer> trainers = traineeService.getTrainers(userNameToFind, credentials);
        return ResponseEntity.ok(traineeMapper.traineeToTraineeResponseDTO(trainee, trainers));
    }
    @GetMapping(path = "/{userName}/trainings")
    public ResponseEntity<List<TrainingTraineeResponseDTO>> getTrainings(@PathVariable final String userName,
                                                                         @RequestParam final Date from,
                                                                         @RequestParam final Date to,
                                                                         @RequestParam final String trainerUserName,
                                                                         @RequestParam final String trainingType,
                                                                         @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        Trainee trainee = traineeService.getTraineeByUserName(userName, credentials).orElse(null);
        if (trainee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Training> trainings = traineeService.getTrainings(userName, from, to, trainerUserName, trainingType, credentials);
        return ResponseEntity.ok(TrainingMapper.TrainingListToTrainingTraineeResponseDTOList(trainings));
    }

    @GetMapping(path = "/{userName}/activeTrainersNotAssigned")
    public ResponseEntity<List<TrainerDTOForList>> getActiveTrainersNotAssignedTo(@PathVariable final String userName, @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        Trainee trainee = traineeService.getTraineeByUserName(userName, credentials).orElse(null);
        if (trainee == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Trainer> trainers = traineeService.getActiveTrainersNotAssignedTo(userName, credentials);
        return ResponseEntity.ok(TrainerMapper.trainerListToTrainerDTOForList(trainers));
    }

    @PutMapping(path = "/updateTrainee")
    public ResponseEntity<TraineeResponseDTO> updateTrainee(@RequestBody final TraineeRequestDTO traineeRequestDTO, @RequestHeader final String userName, @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        TraineeDTO traineeDTO = traineeMapper.traineeRequestDTOToTraineeDTO(traineeRequestDTO);
        Trainee trainee = traineeService.updateTrainee(traineeDTO, credentials).orElse(null);
        if (trainee == null) {
            return ResponseEntity.notFound().build();
        }
        List<Trainer> trainers = traineeService.getTrainers(trainee.getUser().getUserName(), credentials);
        return ResponseEntity.ok(traineeMapper.traineeToTraineeResponseDTO(trainee, trainers));
    }

    @DeleteMapping(path = "/deleteTrainee")
    public ResponseEntity<Void> deleteTrainee(@RequestParam final String userNameToDelete, @RequestHeader final String userName, @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        if (!traineeService.deleteTrainee(userNameToDelete, credentials)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{userName}/updateTrainersList")
    public ResponseEntity<List<TrainerDTOForList>> updateTrainersList(@PathVariable final String userName, @RequestHeader final String password, @RequestBody final List<String> trainersUserNames) {
        Credentials credentials = new Credentials(userName, password);
        if (traineeService.getTraineeByUserName(userName, credentials).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Trainer> result = traineeService.updateTrainersList(userName, trainersUserNames, credentials);
        return ResponseEntity.ok(TrainerMapper.trainerListToTrainerDTOForList(result));
    }

    @PatchMapping(path = "/{userName}/activate-deactivate")
    public ResponseEntity<Void> activateDeactivate(@PathVariable final String userName, @RequestParam final Boolean isActive, @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        if (traineeService.getTraineeByUserName(userName, credentials).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        traineeService.changeActiveStatus(isActive, credentials);
        if (isActive) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
