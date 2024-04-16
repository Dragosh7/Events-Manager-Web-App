package com.example.events.Observer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventPublisherConfig {

    @Bean
    public EventPublisher eventPublisher() {
        EventPublisher eventPublisher = new EventPublisher();
        eventPublisher.registerObserver(new UserNotificationService());
        return eventPublisher;
    }
}
