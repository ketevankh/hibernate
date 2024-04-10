package com.example.task_hibernate.mapper;

import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.User;
import com.example.task_hibernate.model.dto.controllerDTOs.request.TraineeRequestDTO;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TraineeDTOForList;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TrainerResponseDTO;
import com.example.task_hibernate.model.dto.serviceDTOs.TraineeDTO;
import com.example.task_hibernate.model.dto.serviceDTOs.TrainerDTO;
import com.example.task_hibernate.model.dto.serviceDTOs.UserDTO;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TrainerDTOForList;
import com.example.task_hibernate.model.dto.controllerDTOs.request.TrainerRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainerMapper {
    public TrainerDTO trainerControllerDTOToTrainerDTO(final TrainerRequestDTO trainerRequestDTO) {
        return new TrainerDTO(
                trainerRequestDTO.getSpecialization(),
                new UserDTO(
                        trainerRequestDTO.getFirstName(),
                        trainerRequestDTO.getLastName(),
                        true
                )
        );
    }
    public static TrainerDTOForList trainerToTrainerDTOForList(Trainer trainer) {
        return new TrainerDTOForList(
                trainer.getUser().getUserName(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getTrainingType()
        );
    }
    public static List<TrainerDTOForList> trainerListToTrainerDTOForList(List<Trainer> trainers) {
        List<TrainerDTOForList> trainerDTOForLists = new ArrayList<>();
        for (Trainer trainer : trainers) {
            trainerDTOForLists.add(trainerToTrainerDTOForList(trainer));
        }
        return trainerDTOForLists;
    }

    public TrainerResponseDTO trainerToTraineeResponseDTO(Trainer trainer, List<Trainee> trainees) {
        User user = trainer.getUser();
        List<TraineeDTOForList> traineesList = new ArrayList<>();
        for(Trainee trainee : trainees) {
            traineesList.add(new TraineeDTOForList(
                    trainee.getUser().getUserName(),
                    trainee.getUser().getFirstName(),
                    trainee.getUser().getLastName()
            ));
        }
        return new TrainerResponseDTO(user.getFirstName(), user.getLastName(),
                trainer.getTrainingType(), user.getIsActive(), traineesList);
    }

    public TrainerDTO trainerRequestDTOToTrainerDTO(final TrainerRequestDTO trainerRequestDTO) {
        UserDTO user = new UserDTO(trainerRequestDTO.getFirstName(), trainerRequestDTO.getLastName(), trainerRequestDTO.getIsActive());
        return new TrainerDTO(trainerRequestDTO.getSpecialization(), user);
    }
}
