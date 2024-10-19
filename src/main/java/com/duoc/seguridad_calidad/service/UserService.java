package com.duoc.seguridad_calidad.service;

import com.duoc.seguridad_calidad.model.User;
import com.duoc.seguridad_calidad.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private static final String USERS_DIRECTORY = "users";

    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByUsername(String username) {
        try {
            List<User> users = fileStorageUtil.readAllFromDirectory(USERS_DIRECTORY, User.class);
            return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            System.out.println("Error reading user data: " + e.getMessage());
            throw new RuntimeException("Error reading user data", e);
        }
    }

    public void registerUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Set default role
        try {
            System.out.println("Attempting to save user: " + user.getUsername());
            fileStorageUtil.saveToFile(user, USERS_DIRECTORY + "/" + user.getId() + ".json");
            System.out.println("User saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
            throw new RuntimeException("Error saving user data", e);
        }
    }

    public void updateUser(User user) {
        User existingUser = getUserByUsername(user.getUsername());
        if (existingUser != null) {
            user.setId(existingUser.getId());
            if (user.getRole() == null) {
                user.setRole(existingUser.getRole());
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            try {
                fileStorageUtil.saveToFile(user, USERS_DIRECTORY + "/" + user.getId() + ".json");
            } catch (IOException e) {
                throw new RuntimeException("Error updating user data", e);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
}