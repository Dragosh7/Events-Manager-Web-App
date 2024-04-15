package com.example.events.DTOs;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegistrationRequest {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String emailAddress;
}

