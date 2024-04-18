package com.example.events.Service;

import com.example.events.Entity.Event;
import com.example.events.Entity.Ticket;
import com.example.events.Entity.User;
import com.example.events.Observer.EventPublisher;
import com.example.events.Repository.EventRepository;
import com.example.events.Repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

//@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;
    private final TicketService ticketService;
    private final UserService userService;
    private final EventPublisher eventPublisher;


    public EventService(@Lazy EventRepository eventRepository,@Lazy TicketService ticketService,@Lazy UserService userService, @Lazy EventPublisher eventPublisher) {

        this.eventRepository = eventRepository;
        this.ticketService = ticketService;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

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
            String role = activeUser.getRole().toString();
            if ("organizer".equalsIgnoreCase(role) || "admin".equalsIgnoreCase(role)) {
                if(eventRepository.existsByName(event.getName())){

                    if(userService.isUserAllowedToUpdateEvent(activeUser.getId(), event.getId()))
                    {
                        Event existingEvent = eventRepository.findById(event.getId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
                        event.setOrganizer(existingEvent.getOrganizer());

                        subscribeAllUsers(event);
                        eventPublisher.notifyObservers(event,"Check out what's new on this "+event.getName()+" event");

                        return eventRepository.save(event);
                    }
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to modify this event");
                }
                    else{
                         event.setOrganizer(activeUser);

                     subscribeAllUsers(event);
                    eventPublisher.notifyObservers(event,"New event organized, buy a ticket at "+event.getName());

                    return eventRepository.save(event);
                    }

            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to modify events");        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Log in as organizer first");    }

    public void deleteEventById(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Ticket> tickets = ticketService.getTickets(eventId);
        for (Ticket ticket : tickets) {
            ticket.setEvent(null);
        }

        ticketService.saveAll(tickets);

        eventRepository.deleteById(eventId);
    }
    public Event getEventByName(String eventName) {
        return eventRepository.findByName(eventName);
    }
    private void subscribeAllUsers(Event event) {
        List<User> users = userService.retrieveAllUsers();
        for (User user : users) {
            eventPublisher.subscribe(user, event);
        }
    }

}
