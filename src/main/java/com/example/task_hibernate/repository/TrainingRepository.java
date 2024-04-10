package com.example.task_hibernate.repository;

import com.example.task_hibernate.model.Trainee;
import com.example.task_hibernate.model.Trainer;
import com.example.task_hibernate.model.Training;
import com.example.task_hibernate.model.TrainingType;
import com.example.task_hibernate.model.enums.TrainingTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    Optional<Training> findByTrainingName(String title);

    @Query("SELECT DISTINCT t.trainer FROM Training t " +
            "JOIN t.trainee trainee " +
            "JOIN t.trainer trainer " +
            "WHERE trainee.user.userName = :username")
    List<Trainer> findTrainerByTraineeUsername(String username);

    @Query("SELECT DISTINCT t.trainer FROM Training t " +
            "JOIN t.trainee trainee " +
            "JOIN t.trainer trainer " +
            "WHERE trainer.user.userName = :username")
    List<Trainee> findTraineeByTrainerUsername(String username);

    @Query("SELECT tt FROM TrainingType tt")
    List<TrainingType> findAllTrainingTypes();

    @Query("SELECT t FROM Training t " +
            "JOIN t.trainee.user u " +
            "LEFT JOIN t.trainer.user trainerU " +
            "LEFT JOIN t.trainingType tt " +
            "WHERE u.userName = :username " +
            "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
            "AND (:trainerName IS NULL OR trainerU.userName = :trainerName) " +
            "AND (:trainingType IS NULL OR tt.trainingType = :trainingType)")
    List<Training> findByTraineeUsernameAndCriteria(
            @Param("username") String username,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("trainerName") String trainerName,
            @Param("trainingType") TrainingTypeEnum trainingType
    );

    @Query("SELECT t FROM Training t " +
            "JOIN t.trainer.user u " +
            "LEFT JOIN t.trainee.user traineeU " +
            "WHERE u.userName = :trainerUsername " +
            "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
            "AND (:traineeName IS NULL OR traineeU.userName = :traineeName)")
    List<Training> findByTrainerUsernameAndCriteria(
            @Param("trainerUsername") String trainerUsername,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("traineeName") String traineeName
    );

    @Query("DELETE FROM Training t " +
            "WHERE t.trainee.user.userName = :traineeUsername " +
            "AND t.trainer.user.userName IN :trainerUsernames")
    Boolean deleteTraineesTrainingsWithTrainers(@Param("traineeUsername") String traineeUsername, @Param("trainerUsernames") List<String> trainerUsernames);

}
