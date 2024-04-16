package com.example.events.DTOs;

import lombok.Data;

@Data
public class ReservationRequest {
    private String eventName;
    private String ticketType;
    private int quantity;
}

