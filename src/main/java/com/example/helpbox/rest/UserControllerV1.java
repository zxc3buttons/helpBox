package com.example.helpbox.rest;

import com.example.helpbox.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/users")
public class UserControllerV1 {

    private List<User> USERS = Stream.of(
            new User(1L, "Ivan", "Ivanov"),
            new User(2L, "Sergey", "Sergeev"),
            new User(3L, "Petr", "Petrov")
    ).collect(Collectors.toList());

    @GetMapping
    public List<User> getAll() {
        return USERS;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return USERS.stream().filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
