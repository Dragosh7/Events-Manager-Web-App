package com.example.events.Observer;

import com.example.events.Entity.Event;

public interface EventSubject {
    void registerObserver(EventObserver observer);
    void removeObserver(EventObserver observer);
    void notifyObservers(Event event);
}
