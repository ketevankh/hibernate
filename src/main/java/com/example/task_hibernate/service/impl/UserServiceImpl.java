package com.example.task_hibernate.service.impl;

import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.model.User;
import com.example.task_hibernate.model.dto.UserDTO;
import com.example.task_hibernate.repository.UserRepository;
import com.example.task_hibernate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final int PASSWORD_LENGTH = 10;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setIsActive(userDTO.getIsActive());
        user.setUserName(generateUserName(userDTO.getFirstName(), userDTO.getLastName()));
        user.setPassword(generatePassword());

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public boolean changeUserPassword(String password, String userName) {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isEmpty()) {
            log.error("User with username {} not found", userName);
            return false;
        }
        User updateUser = user.get();
        if (updateUser.getPassword().equals(password)) {
            log.error("User with username {} is already using the password", userName);
            return false;
        }
        updateUser.setPassword(password);
        userRepository.save(updateUser);
        return true;
    }

    @Override
    public Optional<User> updateUser(Long id, UserDTO userDTO) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            log.error("User with id {} not found", id);
            return Optional.empty();
        }
        User updateUser = user.get();
        updateUser.setFirstName(userDTO.getFirstName());
        updateUser.setLastName(userDTO.getLastName());
        updateUser.setIsActive(userDTO.getIsActive());
        return Optional.of(userRepository.save(updateUser));
    }

    public boolean changeActiveStatus(Long id, Boolean isActive) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            log.error("User with id {} not found", id);
            return false;
        }
        User updateUser = user.get();
        if (updateUser.getIsActive().equals(isActive)) {
            log.error("User with id {} is already {}", id, isActive ? "active" : "inactive");
            return false;
        }
        updateUser.setIsActive(isActive);
        userRepository.save(updateUser);
        return true;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean validateUserFailed(Credentials credentials) {
        Optional<User> user = userRepository.findByUserName(credentials.userName());
        return !user.isPresent() || !user.get().getPassword().equals(credentials.password());
    }

    @Override
    public String generatePassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, PASSWORD_LENGTH);
    }

    @Override
    public String generateUserName(String firstName, String lastName) {
        String userName = firstName + "." + lastName;
        if (!isUniqueUserName(userName)) {
            int i = 1;
            while (!isUniqueUserName(userName + i)) {
                i++;
            }
            userName = userName + i;
        }
        return userName;
    }

    private boolean isUniqueUserName(String userName) {
        return userRepository.findByUserName(userName).isEmpty();
    }

}
