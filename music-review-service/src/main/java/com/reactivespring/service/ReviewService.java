package com.reactivespring.service;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Mono<Review> addReviews(Review review) {
        return reviewRepository.save(review);
    }

    public Flux<Review> getAllReviews(Long musicInfoId) {
        var reviews = reviewRepository.findReviewsByMusicInfoId(musicInfoId);
        if(reviews != null) {
            return reviews;
        }
        return reviewRepository.findAll();
    }

    public Mono<Review> updateExistingReview(Review updatedReview, String reviewId) {
        return reviewRepository.findById(reviewId)
                .flatMap(review -> {
                    review.setReviewId(updatedReview.getReviewId());
                    review.setComment(updatedReview.getComment());
                    review.setRating(updatedReview.getRating());
                    return reviewRepository.save(review);
                });


    }

    public Mono<Void> deleteReview(String id) {
        return reviewRepository.deleteById(id);
    }
}
