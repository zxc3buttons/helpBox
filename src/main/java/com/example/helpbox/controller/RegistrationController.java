package com.example.helpbox.controller;

import com.example.helpbox.model.Role;
import com.example.helpbox.model.Status;
import com.example.helpbox.model.User;
import com.example.helpbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

import java.util.Optional;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "greeting";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());

        model.addAttribute("user", user);

        if(bindingResult.hasErrors())
            return "greeting";

        if (userFromDb.isPresent()) {
            return "greeting";
        }

        //user.setEmail();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);
        userRepository.save(user);

        return "redirect:/auth/login";
    }
}
