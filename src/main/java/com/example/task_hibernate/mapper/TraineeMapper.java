package com.example.task_hibernate.mapper;

import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.User;
import com.example.task_hibernate.model.dto.serviceDTOs.TraineeDTO;
import com.example.task_hibernate.model.dto.serviceDTOs.UserDTO;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TraineeResponseDTO;
import com.example.task_hibernate.model.dto.controllerDTOs.response.TrainerDTOForList;
import com.example.task_hibernate.model.dto.controllerDTOs.request.TraineeRequestDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TraineeMapper {
    public TraineeDTO traineeRequestDTOToTraineeDTO(final TraineeRequestDTO traineeRequestDTO) {
        UserDTO user = new UserDTO(traineeRequestDTO.getFirstName(), traineeRequestDTO.getLastName(), traineeRequestDTO.getIsActive());
        String address = traineeRequestDTO.getAddress();
        Date dateOfBirth = traineeRequestDTO.getDateOfBirth();
        return new TraineeDTO(address, dateOfBirth, user);
    }
    public TraineeResponseDTO traineeToTraineeResponseDTO(Trainee trainee, List<Trainer> trainers) {
        User user = trainee.getUser();
        List<TrainerDTOForList> trainersList = TrainerMapper.trainerListToTrainerDTOForList(trainers);
        return new TraineeResponseDTO(user.getFirstName(), user.getLastName(),
                trainee.getDateOfBirth(), trainee.getAddress(),
                user.getIsActive(), trainersList);

    }
}
