package com.example.task_hibernate;

import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.User;
import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.service.TraineeService;
import com.example.task_hibernate.service.TrainerService;
import com.example.task_hibernate.service.TrainingService;
import com.example.task_hibernate.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootApplication
public class TaskHibernateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskHibernateApplication.class, args);
    }
}
