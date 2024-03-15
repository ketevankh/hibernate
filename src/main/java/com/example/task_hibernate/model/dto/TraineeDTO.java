package com.example.task_hibernate.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TraineeDTO {
    private String address;
    private Date dateOfBirth;
    private UserDTO user;
}
