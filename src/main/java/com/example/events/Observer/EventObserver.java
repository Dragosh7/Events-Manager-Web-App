package com.example.events.Observer;

import com.example.events.Entity.Event;

public interface EventObserver {
    void update(Event event);
    void update(Event event, String message);
}
