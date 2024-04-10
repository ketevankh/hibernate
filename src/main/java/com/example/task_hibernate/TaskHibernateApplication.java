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
//        ConfigurableApplicationContext context = SpringApplication.run(TaskHibernateApplication.class, args);
//
//        try {
//            UserService userService = context.getBean(UserService.class);
//            TraineeService traineeService = context.getBean(TraineeService.class);
//            TrainerService trainerService = context.getBean(TrainerService.class);
//            TrainingService trainingService = context.getBean(TrainingService.class);
//
//            context.getBean(TransactionTemplate.class).execute(status -> {
//                User user = userService.getUserById(1L).orElseThrow(() -> new RuntimeException("User not found"));
//                Credentials credentials = new Credentials(user.getUserName(), user.getPassword());
//                Trainee trainee = traineeService.getTraineeById(1L, credentials).orElseThrow(() -> new RuntimeException("Trainee not found"));
//                System.out.println("Trainee: " + trainee.getUser().getUserName() + " with UserId# " + trainee.getUser().getId());
//
//                user = userService.getUserById(3L).orElseThrow(() -> new RuntimeException("User not found"));
//                credentials = new Credentials(user.getUserName(), user.getPassword());
//                Trainer trainer = trainerService.getTrainerById(1L, credentials).orElseThrow(() -> new RuntimeException("Trainer not found"));
//                System.out.println("Trainer: " + trainer.getUser().getUserName() + " with UserId# " + trainer.getUser().getId());
//
//                Training training = trainingService.getTrainingById(1L).orElseThrow(() -> new RuntimeException("Training not found"));
//                System.out.println("Training: " + training.getTrainingName() + " for TrainerId# " + training.getTrainer().getId() + " with TraineeId# " + training.getTrainee().getId());
//
//                return null;
//            });
//        } finally {
//            context.close();
//        }
        SpringApplication.run(TaskHibernateApplication.class, args);
    }
}
