package com.example.events.Service;

import com.example.events.DTOs.ReservationRequest;
import com.example.events.Entity.Event;
import com.example.events.Entity.Reservation;
import com.example.events.Entity.Ticket;
import com.example.events.Entity.User;
import com.example.events.Repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final EventService eventService;
    private final UserService userService;
    private final TicketService ticketService;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public Reservation createReservation(ReservationRequest reservationRequest) {

        Event event = eventService.getEventByName(reservationRequest.getEventName());
        User user = userService.findWhichUserIsActive().orElse(null);

        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Log in first or create an account");

        if (event == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such event found");

        Ticket ticket = ticketService.ticketExistsWithTicketType(reservationRequest.getTicketType(), event);
        if (ticket == null || ticket.getAvailability() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tickets found for this event");

        if (reservationRequest.getQuantity() > ticket.getAvailability())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Few tickets left. Can't sell that much");

        ticketService.sellTicket(ticket.getId(), reservationRequest.getQuantity());
        Reservation existingReservation = reservationRepository.findByUserAndTicketEvent(user, event);

        if (existingReservation != null) {
            int newQuantity = existingReservation.getQuantity() + reservationRequest.getQuantity();
            existingReservation.setQuantity(newQuantity);
            return reservationRepository.save(existingReservation);
        }
        Reservation reservation = Reservation.builder()
                .user(user)
                .ticket(ticket)
                .quantity(reservationRequest.getQuantity())
                .date(LocalDate.now())
                .build();

        return reservationRepository.save(reservation);
    }

    public void deleteReservationById(Long id) {

        reservationRepository.deleteById(id);
    }

    public Reservation getReservationsByUserAndEvent(User user, Event event) {
        return reservationRepository.findByUserAndTicketEvent(user, event);
    }
}
