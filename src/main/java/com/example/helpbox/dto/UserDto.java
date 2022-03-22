package com.example.helpbox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
    private String userRole;
    private String userStatus;
}
