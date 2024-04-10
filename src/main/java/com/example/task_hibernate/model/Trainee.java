package com.example.task_hibernate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;

@Entity
@Table(name="TRAINEES")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String address;

    @Column()
    @Nullable
    private Date dateOfBirth;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private User user;
}
