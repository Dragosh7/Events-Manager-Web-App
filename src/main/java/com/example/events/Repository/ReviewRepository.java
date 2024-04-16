package com.example.events.Repository;

import com.example.events.Entity.Event;
import com.example.events.Entity.Review;
import com.example.events.Entity.Ticket;
import com.example.events.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByEventAndUser(Event event, User user);
}
