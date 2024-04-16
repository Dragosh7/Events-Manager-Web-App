package com.example.events.Repository;

import com.example.events.Entity.Event;
import com.example.events.Entity.Reservation;
import com.example.events.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByUserAndTicketEvent(User user, Event event);
}
