package com.example.events.Controller;

import com.example.events.DTOs.ReviewRequest;
import com.example.events.Entity.Review;
import com.example.events.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest review) {
        Review createdReview = reviewService.saveReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PutMapping("/")
    public ResponseEntity<Review> updateReview(@RequestBody ReviewRequest review) {
        Review createdReview = reviewService.saveReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @DeleteMapping("/{eventName}")
    public ResponseEntity<String> deleteReview(@PathVariable String eventName) {
        reviewService.deleteReviewOfUser(eventName);
        return ResponseEntity.ok("Review posted at "+eventName+" deleted successfully");
    }

}
