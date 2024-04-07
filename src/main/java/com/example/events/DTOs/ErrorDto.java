package com.example.events.DTOs;


import lombok.Builder;

@Builder
public record ErrorDto (String message) { }
