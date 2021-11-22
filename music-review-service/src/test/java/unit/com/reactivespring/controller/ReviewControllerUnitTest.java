package com.reactivespring.controller;

import com.reactivespring.domain.Review;
import com.reactivespring.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;

@AutoConfigureWebTestClient
@WebFluxTest(controllers = ReviewController.class)
class ReviewControllerUnitTest {

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private WebTestClient webTestClient;

    private static String REVIEWS_ENDPOINT = "/v1/reviews";

    @Test
    public void addReviews() {
     /*
        private String reviewId;
        private Long musicInfoId;
        private String comment;
        private Double rating;

     */

        var newReview = new Review("",1L,"This is a test comment for music abc",5.0);

        Mockito.when(reviewService.addReviews(isA(Review.class)))
                        .thenReturn(Mono.just(newReview));
        webTestClient.post()
                .uri(REVIEWS_ENDPOINT)
                .bodyValue(newReview)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    var result = reviewEntityExchangeResult.getResponseBody();
                    assertNotNull(result);
                    assertEquals(5.0,result.getRating());
                });
    }

    @Test
    public void getReviews() {

        var allReviews = List.of
                (new Review("",1L,"This is a test comment for music abc",5.0));

        Mockito.when(reviewService.getAllReviews(isA(Long.class)))
                .thenReturn(Flux.fromIterable(allReviews));

        webTestClient.get()
                .uri(uriBuilder ->
                    uriBuilder.path(REVIEWS_ENDPOINT)
                            .queryParam("id", "1")
                            .build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .hasSize(1);
    }

    @Test
    public void updateExistingReview() {
     /*
        private String reviewId;
        private Long musicInfoId;
        private String comment;
        private Double rating;

     */

        var newReview = new Review("abc",1L,"This is a test comment for music abc",5.0);

        Mockito.when(reviewService.updateExistingReview(isA(Review.class),isA(String.class)))
                .thenReturn(Mono.just(newReview));

        webTestClient.put()
                .uri(REVIEWS_ENDPOINT+"/{id}","abc")
                .bodyValue(newReview)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    var result = reviewEntityExchangeResult.getResponseBody();
                    assertNotNull(result);
                    assertEquals(5.0,result.getRating());
                    assertEquals("abc",result.getReviewId());
                });
    }

    @Test
    public void deleteReview() {
     /*
        private String reviewId;
        private Long musicInfoId;
        private String comment;
        private Double rating;

     */

        Mockito.when(reviewService.deleteReview(isA(String.class)))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(REVIEWS_ENDPOINT+"/{id}","abc")
                .exchange()
                .expectStatus()
                .isNoContent();

    }

}