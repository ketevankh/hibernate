package com.example.task_hibernate.controller;

import com.example.task_hibernate.model.TrainingType;
import com.example.task_hibernate.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @GetMapping(path = "/trainingTypes")
    public ResponseEntity<List<TrainingType>> getTrainingTypes() {
        return ResponseEntity.ok(trainingService.getAllTrainingTypes());
    }

    @PostMapping(path = "/addTraining")
    public ResponseEntity<Void> addTraining(@RequestParam String traineeUserName,
                                            @RequestParam String trainerUserName,
                                            @RequestParam String trainingName,
                                            @RequestParam Date trainingDate,
                                            @RequestParam int trainingDuration) {
        if (!trainingService.addTraining(traineeUserName, trainerUserName, trainingName, trainingDate, trainingDuration)) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
