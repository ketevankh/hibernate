-- Inserting initial data for TRAINING_TYPES table
INSERT INTO training_types (id, training_type) VALUES (1, 'CARDIO');
INSERT INTO training_types (id, training_type) VALUES (2, 'STRENGTH');
INSERT INTO training_types (id, training_type) VALUES (3, 'FLEXIBILITY');
INSERT INTO training_types (id, training_type) VALUES (4, 'BALANCE');

-- Inserting initial data for USERS table
INSERT INTO USERS (first_name, last_name, user_name, password, is_active) VALUES
                                                                              ('John', 'Doe', 'John.Doe', 'password123', true),
                                                                              ('Alice', 'Smith', 'Alice.Smith', 'abc123', true),
                                                                              ('Bob', 'Johnson', 'Bob.Johnson', 'def456', true),
                                                                              ('Charlie', 'Brown', 'Charlie.Brown', 'ghi789', true);

-- Inserting initial data for TRAINEES table
INSERT INTO TRAINEES (address, date_of_birth, user_id) VALUES
                                                           ('123 Main St, Cityville', '1990-05-15', 1),
                                                           ('456 Elm St, Townsville', '1992-09-20', 2);

-- Inserting initial data for TRAINERS table
INSERT INTO TRAINERS (user_id, training_type_id) VALUES
    (3, 1);

-- Inserting initial data for TRAININGS table
INSERT INTO TRAININGS (trainee_id, trainer_id, training_type_id, training_name, training_date, duration) VALUES
    (2, 1, 2, 'Strength Session 1', '2022-01-02', 90);