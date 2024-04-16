package com.example.events.Observer;

import com.example.events.Entity.Event;
import com.example.events.Observer.EventObserver;

public class UserNotificationService implements EventObserver {
    @Override
    public void update(Event event) {
        System.out.println("Notification that a new event appeared - Event: " + event.getName());
    }

    @Override
    public void update(Event event, String message) {
        System.out.println("Notification: " + message + " - Event: " + event.getName());
    }
}
