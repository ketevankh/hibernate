package com.example.task_hibernate.mapper;

import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TrainingTraineeResponseDTO;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TrainingTrainerResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class TrainingMapper {
    public static List<TrainingTraineeResponseDTO>TrainingListToTrainingTraineeResponseDTOList(List<Training> trainings)  {
        List<TrainingTraineeResponseDTO> result = new ArrayList<>();
        for (Training training : trainings) {
            result.add(new TrainingTraineeResponseDTO(training.getTrainingName(), training.getTrainingType().getTrainingType().name(),
                    training.getTrainingDate(), training.getDuration(), training.getTrainer().getUser().getUserName()));
        }
        return result;
    }

    public static List<TrainingTrainerResponseDTO> TrainingListToTrainingTrainerResponseDTOList(List<Training> trainings)  {
        List<TrainingTrainerResponseDTO> result = new ArrayList<>();
        for (Training training : trainings) {
            result.add(new TrainingTrainerResponseDTO(training.getTrainingName(), training.getTrainingType().getTrainingType().name(),
                    training.getTrainingDate(), training.getDuration(), training.getTrainee().getUser().getUserName()));
        }
        return result;
    }
}
