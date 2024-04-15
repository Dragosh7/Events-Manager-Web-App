package com.example.events.Mapper;

import com.example.events.DTOs.UserDto;
import com.example.events.Entity.User;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {

    public UserDto userEntityToDto(User user){
        return UserDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emailAddress(user.getEmailAddress())
                .build();
    }

    public User userDtoToEntity(UserDto userDto, String password){
        return User.builder()
                .username(userDto.getUsername())
                .password(password)
                .role((userDto.getRole()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .emailAddress(userDto.getEmailAddress())
                .build();
    }

}
