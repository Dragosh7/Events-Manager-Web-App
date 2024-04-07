package com.example.events.DTOs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
public record ErrorDto (String message) { }
