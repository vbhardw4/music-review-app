package com.reactivespring.controller;

import com.reactivespring.domain.Review;
import com.reactivespring.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    public Mono<Review> addReviews(@RequestBody Review review) {
        return reviewService.addReviews(review).log();
    }

    @GetMapping("/reviews")
    public Flux<Review> getReviews(@RequestParam("musicInfoId") String musicInfoId) {
        return reviewService.getAllReviews(Long.valueOf(musicInfoId));
    }

    @PutMapping("/reviews/{id}")
    public Mono<Review> updateExistingReview(@RequestBody Review updatedReview,@PathVariable String id) {
        return reviewService.updateExistingReview(updatedReview,id);
    }

    @DeleteMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteReview(@PathVariable String id) {
        return reviewService.deleteReview(id);
    }

}
