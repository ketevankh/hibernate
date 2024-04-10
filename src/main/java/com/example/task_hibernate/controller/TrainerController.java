package com.example.task_hibernate.controller;

import com.example.task_hibernate.mapper.TrainerMapper;
import com.example.task_hibernate.mapper.TrainingMapper;
import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.model.dto.controllerDTOs.request.TrainerRequestDTO;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TrainerResponseDTO;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TrainingTrainerResponseDTO;
import com.example.task_hibernate.model.dto.serviceDTOs.TrainerDTO;
import com.example.task_hibernate.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;


    @GetMapping(path = "test")
    public String test() {
        return "Test";
    }

    @PostMapping(path = "/register")
    @Operation(summary = "Register a new trainer")
    public ResponseEntity<Credentials> registerTrainer(@RequestBody final TrainerDTO trainerDTO) {
        Trainer trainer = trainerService.createTrainer(trainerDTO);
        Credentials credentials = new Credentials(trainer.getUser().getUserName(), trainer.getUser().getPassword());
        return ResponseEntity.ok(credentials);
    }

    @GetMapping(path = "/{userNameToFind}")
    public ResponseEntity<TrainerResponseDTO> getTrainerByUserName(@PathVariable final String userNameToFind, @RequestHeader final String userName, @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        Trainer trainer = trainerService.getTrainerByUserName(userNameToFind, credentials).orElse(null);
        if (trainer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Trainee> trainees = trainerService.getTrainees(userNameToFind, credentials);
        return ResponseEntity.ok(trainerMapper.trainerToTraineeResponseDTO(trainer, trainees));
    }

    @PatchMapping(path = "/{userName}/activate-deactivate")
    public ResponseEntity<Void> activateDeactivate(@PathVariable final String userName, @RequestParam final Boolean isActive, @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        if (trainerService.getTrainerByUserName(userName, credentials).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        trainerService.changeActiveStatus(isActive, credentials);
        if (isActive) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping(path = "/updateTrainer")
    public ResponseEntity<TrainerResponseDTO> updateTrainer(@RequestBody final TrainerRequestDTO trainerRequestDTO, @RequestHeader final String userName, @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        TrainerDTO trainerDTO = trainerMapper.trainerRequestDTOToTrainerDTO(trainerRequestDTO);
        Trainer trainer = trainerService.updateTrainer(trainerDTO, credentials).orElse(null);
        if (trainer == null) {
            return ResponseEntity.notFound().build();
        }
        List<Trainee> trainees = trainerService.getTrainees(trainer.getUser().getUserName(), credentials);
        return ResponseEntity.ok(trainerMapper.trainerToTraineeResponseDTO(trainer, trainees));
    }

    @GetMapping(path = "/{userName}/trainings")
    public ResponseEntity<List<TrainingTrainerResponseDTO>> getTrainings(@PathVariable final String userName,
                                                                         @RequestParam final Date from,
                                                                         @RequestParam final Date to,
                                                                         @RequestParam final String trainerUserName,
                                                                         @RequestParam final String trainingType,
                                                                         @RequestHeader final String password) {
        Credentials credentials = new Credentials(userName, password);
        Trainer trainer = trainerService.getTrainerByUserName(userName, credentials).orElse(null);
        if (trainer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Training> trainings = trainerService.getTrainings(userName, from, to, trainerUserName, trainingType, credentials);
        return ResponseEntity.ok(TrainingMapper.TrainingListToTrainingTrainerResponseDTOList(trainings));
    }
}
