package com.example.task_hibernate.model.dto.controllerDTOs;


import lombok.Data;

import java.util.Date;

@Data
public class TraineeControllerDTO {
    String firstName;
    String lastName;
    Date dateOfBirth;
    String address;
}
