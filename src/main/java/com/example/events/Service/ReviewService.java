package com.example.events.Service;

import com.example.events.DTOs.ReviewRequest;
import com.example.events.Entity.Event;
import com.example.events.Entity.Reservation;
import com.example.events.Entity.Review;
import com.example.events.Entity.User;
import com.example.events.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final EventService eventService;
    private final ReservationService reservationService;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public Review saveReview(ReviewRequest reviewRequest) {
        Event event = eventService.getEventByName(reviewRequest.getEventName());
        if (event != null && isUserAllowedToReview(event)) {
            Review toUpdate = reviewRepository.findByEventAndUser(event, userService.findWhichUserIsActive().orElseThrow(() -> new RuntimeException("Log in or register: " )));
            if (toUpdate != null) {
                //throw new IllegalArgumentException("User has already reviewed this event");
                toUpdate.setRating(reviewRequest.getRating());
                toUpdate.setComment(reviewRequest.getComment());
                return reviewRepository.save(toUpdate);

            }
            Review review = Review.builder()
                    .event(event)
                    .user(userService.findWhichUserIsActive().orElseThrow(() -> new RuntimeException("Log in or register: " )))
                    .rating(reviewRequest.getRating())
                    .comment(reviewRequest.getComment())
                    .build();
            return reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("User is not allowed to add a review for this event");
        }
    }

    public void deleteReviewOfUser(String name) {

        Event event = eventService.getEventByName(name);
        Review toDelete = reviewRepository.findByEventAndUser(event, userService.findWhichUserIsActive().orElseThrow(() -> new RuntimeException("Log in or register: " )));

        reviewRepository.delete(toDelete);
    }
    public void deleteReviewById(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
    private boolean isUserAllowedToReview(Event event) {
        Optional<User> userOptional  = userService.findWhichUserIsActive();
        if(userOptional .isPresent()) {
            User user = userOptional.get();
            Reservation reservations = reservationService.getReservationsByUserAndEvent(user, event);
            if(reservations != null){
                return true;
            }

        }
        return false;
    }

}
