package com.example.events.Repository;

import com.example.events.Entity.Event;
import com.example.events.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByName(String name);
    List<Event> findByOrganizer(User organizer);
    Event findByName(String name);
}
