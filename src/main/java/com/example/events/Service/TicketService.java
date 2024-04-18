package com.example.events.Service;

import com.example.events.DTOs.UserDto;
import com.example.events.Entity.Event;
import com.example.events.Entity.Ticket;
import com.example.events.Entity.User;
import com.example.events.Mapper.UserMapper;
import com.example.events.Repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    @Lazy
    private final EventService eventService;


    public void saveAll(List<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }
    public Ticket saveTicket(Ticket ticket) {
        Event event = eventService.getEventById(ticket.getEvent().getId());
        if (event != null) {
            Optional<User> loggedInUserOptional = userService.findWhichUserIsActive();

            if (loggedInUserOptional.isPresent()) {
                User loggedInUser = loggedInUserOptional.get();

                if (userService.isUserAllowedToUpdateEvent(loggedInUser.getId(), event.getId())) {
                    ticket.setEvent(event);
                    ticket.setEventName(event.getName());
                    return ticketRepository.save(ticket);
                }
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Only the organizer of the event can add new tickets");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Create an existing event first");
    }
    public Ticket updateTicket(Long id,Ticket ticket) {
        Event event = eventService.getEventById(ticket.getEvent().getId());
            Optional<User> loggedInUserOptional = userService.findWhichUserIsActive();
            Ticket ifExists = getTicketById(id);
            if(ifExists != null) {
                if (loggedInUserOptional.isPresent()) {
                    User loggedInUser = loggedInUserOptional.get();

                    if (userService.isUserAllowedToUpdateEvent(loggedInUser.getId(), event.getId())) {
                        ifExists.setAvailability(ticket.getAvailability());
                        ifExists.setTicketType(ticket.getTicketType());
                        ifExists.setPrice(ticket.getPrice());
                        return ticketRepository.save(ifExists);
                    }
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the organizer of the event can modify tickets");
                }

                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No permission");
            }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This ticket does not exist");
    }

    public void sellTicket(Long id,Integer toSell){
        Ticket ticket = getTicketById(id);
        ticket.setAvailability(ticket.getAvailability()-toSell);
        ticketRepository.save(ticket);
    }
    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }
    public Ticket ticketExistsWithTicketType(String ticketType, Event event) {
        return ticketRepository.findByTicketTypeAndEvent(ticketType,event).orElse(null);
    }
    public List<Ticket> getTickets(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

}
