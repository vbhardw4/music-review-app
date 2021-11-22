package com.reactivespring.controller;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

/**
 * Will spin up the spring context for me.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/**
 * Provide the active profile
 */
@ActiveProfiles("test")
/**
 * In order to test the end point, we need web test client
 */
@AutoConfigureWebTestClient
public class ReviewControllerIntegrationTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    WebTestClient webTestClient;

    private static String REVIEWS_ENDPOINT = "/v1/reviews";

    @BeforeEach
    void setUp() {
        var reviews = List.of(
                (new Review("",1L,"This is a test comment for music abc",5.0)),
                (new Review("",1L,"This is a test comment 2 for music abc",4.0)));

        /**
         * Blocking calls are only allowed in the test cases
         */
        reviewRepository.saveAll(reviews)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll()
                        .block();
    }
}
