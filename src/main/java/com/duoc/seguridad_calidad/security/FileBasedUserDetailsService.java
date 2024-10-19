package com.duoc.seguridad_calidad.security;

import com.duoc.seguridad_calidad.model.User;
import com.duoc.seguridad_calidad.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FileBasedUserDetailsService implements UserDetailsService {

    private static final String USERS_DIRECTORY = "data/users";

    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            System.out.println("Attempting to load user: " + username);
            List<User> users = fileStorageUtil.readAllFromDirectory(USERS_DIRECTORY, User.class);
            System.out.println("Found " + users.size() + " users in directory");
            return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst()
                    .orElseThrow(() -> {
                        System.out.println("User not found: " + username);
                        return new UsernameNotFoundException("User not found: " + username);
                    });
        } catch (IOException e) {
            System.out.println("Error loading user: " + e.getMessage());
            throw new UsernameNotFoundException("Error loading user", e);
        }
    }
}