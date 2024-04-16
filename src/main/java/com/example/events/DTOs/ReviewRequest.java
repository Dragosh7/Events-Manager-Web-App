package com.example.events.DTOs;

import lombok.Data;

@Data
public class ReviewRequest {
    private String eventName;
    private Integer rating;
    private String comment;
}
