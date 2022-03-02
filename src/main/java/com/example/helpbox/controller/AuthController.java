package com.example.helpbox.controller;

import com.example.helpbox.model.Role;
import com.example.helpbox.model.Status;
import com.example.helpbox.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("user",new User());
        return "login";
    }

    /* @PostMapping("/login")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());

        model.addAttribute("user", user);

        if(bindingResult.hasErrors())
            return "reg";

        if (userFromDb.isPresent()) {
            return "reg";
        }

        //user.setEmail();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);
        userRepository.save(user);

        return "redirect:/auth/login";
    } */

    @GetMapping("/main")
    public String getSuccessPage() {
        return "osnova";
    }
}
