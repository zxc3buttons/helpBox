package com.example.helpbox.config;

import com.example.helpbox.dto.UserDto;
import com.example.helpbox.model.User;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

public class OrikaCfg extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(User.class, UserDto.class)
                .field("id", "userId")
                .field("email", "userEmail")
                .field("firstName", "userFirstName")
                .field("lastName", "userLastName")
                .field("password", "userPassword")
                .field("status", "userStatus")
                .field("role", "userRole")
                .register();
    }
}
