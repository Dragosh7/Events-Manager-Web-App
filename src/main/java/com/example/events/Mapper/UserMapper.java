package com.example.events.Mapper;

import com.example.events.DTOs.SignUpDto;
import com.example.events.DTOs.UserDto;
import com.example.events.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
