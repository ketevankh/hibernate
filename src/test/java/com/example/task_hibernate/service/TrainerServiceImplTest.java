package com.example.task_hibernate.service;

import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.TrainingType;
import com.example.task_hibernate.model.User;
import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.model.dto.TraineeDTO;
import com.example.task_hibernate.model.dto.TrainerDTO;
import com.example.task_hibernate.model.dto.UserDTO;
import com.example.task_hibernate.model.enums.TrainingTypeEnum;
import com.example.task_hibernate.repository.TraineeRepository;
import com.example.task_hibernate.repository.TrainerRepository;
import com.example.task_hibernate.service.impl.TraineeServiceImpl;
import com.example.task_hibernate.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TrainerServiceImplTest {
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTrainers() {
        Credentials credentials = new Credentials("username", "password");

        when(userService.validateUserFailed(credentials)).thenReturn(true);
        assertTrue(trainerService.getAllTrainers(credentials).isEmpty());
        verify(userService, times(1)).validateUserFailed(credentials);

        when(userService.validateUserFailed(credentials)).thenReturn(false);
        when(trainerRepository.findAll()).thenReturn(Collections.singletonList(new Trainer()));
        assertFalse(trainerService.getAllTrainers(credentials).isEmpty());
        verify(userService, times(2)).validateUserFailed(credentials);
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    void getTrainerByUserName() {
        Credentials credentials = new Credentials("username", "password");
        String userName = "testUser";

        when(userService.validateUserFailed(credentials)).thenReturn(true);
        assertFalse(trainerService.getTrainerByUserName(userName, credentials).isPresent());
        verify(userService, times(1)).validateUserFailed(credentials);

        when(userService.validateUserFailed(credentials)).thenReturn(false);
        when(trainerRepository.findByUser_UserName(userName)).thenReturn(Optional.of(new Trainer()));
        assertTrue(trainerService.getTrainerByUserName(userName, credentials).isPresent());
        verify(userService, times(2)).validateUserFailed(credentials);
        verify(trainerRepository, times(1)).findByUser_UserName(userName);
    }

    @Test
    void createTrainer() {
        UserDTO userDTO = new UserDTO("John", "Doe", true);

        TrainingType specialization =new TrainingType(1L, TrainingTypeEnum.CARDIO);

        TrainerDTO trainerDTO = new TrainerDTO(specialization, userDTO);

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setIsActive(userDTO.getIsActive());

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(specialization);

        when(userService.createUser(any(UserDTO.class))).thenReturn(user);
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer result = trainerService.createTrainer(trainerDTO);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(specialization, result.getTrainingType());

        verify(userService, times(1)).createUser(any(UserDTO.class));
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void getTrainerById() {
        Credentials credentials = new Credentials("username", "password");
        Long id = 1L;

        when(userService.validateUserFailed(credentials)).thenReturn(true);
        assertTrue(trainerService.getTrainerById(id, credentials).isEmpty());
        verify(userService, times(1)).validateUserFailed(credentials);

        when(userService.validateUserFailed(credentials)).thenReturn(false);
        when(trainerRepository.findById(id)).thenReturn(Optional.empty());
        assertTrue(trainerService.getTrainerById(id, credentials).isEmpty());
        verify(userService, times(2)).validateUserFailed(credentials);
        verify(trainerRepository, times(1)).findById(id);

        Trainer trainer = new Trainer();
        when(trainerRepository.findById(id)).thenReturn(Optional.of(trainer));
        Optional<Trainer> result = trainerService.getTrainerById(id, credentials);
        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
        verify(userService, times(3)).validateUserFailed(credentials);
        verify(trainerRepository, times(2)).findById(id);
    }

    @Test
    void changeTrainerPassword() {
        Credentials credentials = new Credentials("username", "password");
        String newPassword = "newPassword";

        when(userService.validateUserFailed(credentials)).thenReturn(true);
        assertFalse(trainerService.changeTrainerPassword(newPassword, credentials));
        verify(userService, times(1)).validateUserFailed(credentials);

        when(userService.validateUserFailed(credentials)).thenReturn(false);
        when(userService.changeUserPassword(anyString(), anyString())).thenReturn(false);
        assertFalse(trainerService.changeTrainerPassword(newPassword, credentials));
        verify(userService, times(2)).validateUserFailed(credentials);
        verify(userService, times(1)).changeUserPassword(anyString(), anyString());


        when(userService.changeUserPassword(anyString(), anyString())).thenReturn(true);
        assertTrue(trainerService.changeTrainerPassword(newPassword, credentials));
        verify(userService, times(3)).validateUserFailed(credentials);
        verify(userService, times(2)).changeUserPassword(anyString(), anyString());
    }

    @Test
    void updateTrainer() {
        Credentials credentials = new Credentials("username", "password");
        UserDTO userDTO = new UserDTO("John", "Doe", true);
        TrainingType specialization = new TrainingType(1L, TrainingTypeEnum.CARDIO);
        TrainerDTO trainerDTO = new TrainerDTO(specialization, userDTO);
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        User user = new User();
        user.setId(1L);
        trainer.setUser(user);
        trainer.setTrainingType(specialization);

        when(userService.validateUserFailed(credentials)).thenReturn(true);
        assertTrue(trainerService.updateTrainer(trainer, credentials).isEmpty());
        verify(userService, times(1)).validateUserFailed(credentials);

        when(userService.validateUserFailed(credentials)).thenReturn(false);
        when(trainerRepository.findById(trainer.getId())).thenReturn(Optional.empty());
        assertTrue(trainerService.updateTrainer(trainer, credentials).isEmpty());
        verify(userService, times(2)).validateUserFailed(credentials);
        verify(trainerRepository, times(1)).findById(trainer.getId());

        when(trainerRepository.findById(trainer.getId())).thenReturn(Optional.of(trainer));
        when(userService.updateUser(anyLong(), any(UserDTO.class))).thenReturn(Optional.empty());
        assertTrue(trainerService.updateTrainer(trainer, credentials).isEmpty());
        verify(userService, times(3)).validateUserFailed(credentials);
        verify(trainerRepository, times(2)).findById(trainer.getId());
        verify(userService, times(1)).updateUser(anyLong(), any(UserDTO.class));

        when(userService.updateUser(anyLong(), any(UserDTO.class))).thenReturn(Optional.of(new User()));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        Optional<Trainer> result = trainerService.updateTrainer(trainer, credentials);
        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
        verify(userService, times(4)).validateUserFailed(credentials);
        verify(trainerRepository, times(3)).findById(trainer.getId());
        verify(userService, times(2)).updateUser(anyLong(), any(UserDTO.class));
        verify(trainerRepository, times(1)).save(any(Trainer.class));
    }

    @Test
    void changeActiveStatus() {
        Credentials credentials = new Credentials("username", "password");
        Boolean isActive = true;

        when(userService.validateUserFailed(credentials)).thenReturn(true);
        assertFalse(trainerService.changeActiveStatus(isActive, credentials));
        verify(userService, times(1)).validateUserFailed(credentials);

        when(trainerRepository.findByUser_UserName(credentials.userName())).thenReturn(Optional.of(new Trainer()));
        when(userService.changeActiveStatus(anyLong(), anyBoolean())).thenReturn(false);
        assertFalse(trainerService.changeActiveStatus(isActive, credentials));
        verify(userService, times(2)).validateUserFailed(credentials);

        when(userService.validateUserFailed(credentials)).thenReturn(false);
        when(userService.changeActiveStatus(anyLong(), anyBoolean())).thenReturn(true);
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUserName(credentials.userName());
        user.setId(1L);
        trainer.setUser(user);
        when(trainerRepository.findByUser_UserName(credentials.userName())).thenReturn(Optional.of(trainer));
        when(trainerRepository.findByUser_UserName(credentials.userName())).thenReturn(Optional.of(trainer));
        assertTrue(trainerService.changeActiveStatus(isActive, credentials));
        verify(userService, times(3)).validateUserFailed(credentials);
        verify(userService, times(1)).changeActiveStatus(anyLong(), anyBoolean());
    }

    @Test
    void deleteTrainer() {
        Credentials credentials = new Credentials("username", "password");
        String userName = "testUser";

        when(userService.validateUserFailed(credentials)).thenReturn(true);
        assertFalse(trainerService.deleteTrainer(userName, credentials));
        verify(userService, times(1)).validateUserFailed(credentials);

        when(userService.validateUserFailed(credentials)).thenReturn(false);
        when(trainerRepository.findByUser_UserName(userName)).thenReturn(Optional.empty());
        assertFalse(trainerService.deleteTrainer(userName, credentials));
        verify(userService, times(2)).validateUserFailed(credentials);
        verify(trainerRepository, times(1)).findByUser_UserName(userName);

        when(trainerRepository.findByUser_UserName(userName)).thenReturn(Optional.of(new Trainer()));
        assertTrue(trainerService.deleteTrainer(userName, credentials));
        verify(userService, times(3)).validateUserFailed(credentials);
        verify(trainerRepository, times(2)).findByUser_UserName(userName);
        verify(trainerRepository, times(1)).deleteByUser_UserName(userName);
    }
}
