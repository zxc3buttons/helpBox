package com.example.helpbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HelpBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpBoxApplication.class, args);
    }

}
