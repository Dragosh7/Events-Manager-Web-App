package com.example.events.Service;

import com.example.events.Entity.Event;
import com.example.events.Entity.User;
import com.example.events.Repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
    }

    public Event saveOrUpdateEvent(Event event) {
        Optional<User> activeUserOptional = userService.findWhichUserIsActive();
        if(activeUserOptional.isPresent()) {
            User activeUser = activeUserOptional.get();
            String role = activeUser.getRole();
            if ("organizer".equalsIgnoreCase(role) || "admin".equalsIgnoreCase(role)) {
                if(eventRepository.existsByName(event.getName())){

                    if(userService.isUserAllowedToUpdateEvent(activeUser.getId(), event.getId()))
                    {
                        Event existingEvent = eventRepository.findById(event.getId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
                        event.setOrganizer(existingEvent.getOrganizer());
                        return eventRepository.save(event);
                    }
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to modify this event");
                }
                    else{
                         event.setOrganizer(activeUser);
                     return eventRepository.save(event);
                    }

            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to modify events");        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Log in as organizer first");    }

    public void deleteEventById(Long eventId) {
        eventRepository.deleteById(eventId);
    }
    public Event getEventByName(String eventName) {
        return eventRepository.findByName(eventName);
    }


}
