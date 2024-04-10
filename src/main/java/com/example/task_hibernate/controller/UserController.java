package com.example.task_hibernate.controller;

import com.example.task_hibernate.model.dto.Credentials;
import com.example.task_hibernate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (userService.validateUserFailed(new Credentials(username, password))) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().body("Welcome " + username);
        }
    }

    @PutMapping("/changeLogin")
    public ResponseEntity<Void> changeLogin(@RequestParam String userName, @RequestParam String oldPassword, @RequestParam String newPassword) {
        if (userService.validateUserFailed(new Credentials(userName, oldPassword))) {
            if (userService.changeUserPassword(newPassword, userName)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
