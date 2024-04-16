package com.example.events.Observer;

import com.example.events.Entity.Event;
import com.example.events.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher implements EventSubject {
    private List<EventObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(EventObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(EventObserver observer) {
        observers.remove(observer);
    }

    public void subscribe(User user, Event event) {
        user.subscribeToEvent(event);
    }
    public void notifyObservers(Event event) {
        for (EventObserver observer : observers) {
            observer.update(event);
        }
    }
    public void notifyObservers(Event event,String message) {
        if(message.length() == 0){
         message = "User '" + event.getOrganizer().getUsername() +
                "' created a new COOL event '" + event.getName() + "'.";
        }

        for (EventObserver observer : observers) {
            observer.update(event, message);
        }
    }
}
