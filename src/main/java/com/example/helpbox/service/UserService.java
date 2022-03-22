package com.example.helpbox.service;

import com.example.helpbox.model.Role;
import com.example.helpbox.model.Status;
import com.example.helpbox.model.User;
import com.example.helpbox.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Level;

@Log
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User userRegistration(User user) throws Exception {
        log.log(Level.INFO, user.toString());

        if (this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Пользователь с данным email уже зарегестрирован!");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setStatus(Status.ACTIVE);
            user.setRole(Role.USER);
        }

        return this.userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User userAuthorization(String email, String password) throws Exception {
        User user = this.userRepository.findByEmail(email).orElseThrow();

        if(user == null) {
            throw new Exception("Пользователь с таким email не найден!");
        } else if(!user.getPassword().equals(password.split("\"")[3])) {
            throw new Exception("Пароль введен неверно");
        }

        return user;
    }
}
