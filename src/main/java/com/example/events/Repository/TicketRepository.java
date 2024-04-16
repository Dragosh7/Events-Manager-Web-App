package com.example.events.Repository;

import com.example.events.Entity.Event;
import com.example.events.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByTicketType(String ticketType);
    Optional<Ticket> findByTicketTypeAndEvent(String ticketType, Event event);
}
