package com.example.helpbox.controller;

import com.example.helpbox.config.OrikaCfg;
import com.example.helpbox.dto.UserDto;
import com.example.helpbox.model.User;
import com.example.helpbox.service.UserDetailsServiceImpl;
import com.example.helpbox.service.UserService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final MapperFacade facade = new OrikaCfg();

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<UserDto> registrationUsers(@RequestBody User user) throws Exception {
        UserDto result = this.facade.map(this.userService.userRegistration(user), UserDto.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
