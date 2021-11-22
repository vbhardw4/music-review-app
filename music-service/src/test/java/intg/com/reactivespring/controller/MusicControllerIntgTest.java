package com.reactivespring.controller;

import com.reactivespring.domain.Music;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

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
/**
 * Will spin up the HTTP server in port 8084
 */
@AutoConfigureWireMock(port = 8084)
@TestPropertySource(
        properties = {
                "restClient.musicInfoUrl= http://localhost:8084/v1/musicInfo",
                "restClient.reviewsUrl = http://localhost:8084/v1/reviews"
        }
)
class MusicControllerIntgTest {

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void retrieveMusicById() {

        stubFor(get(urlEqualTo("/v1/musicInfo/"+"abc"))
                .willReturn(aResponse()
                        .withHeader("Content-Type","application/json")
                        .withBodyFile("musicinfo.json")
        ));

        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withHeader("Content-Type","application/json")
                        .withBodyFile("reviews.json")
                ));

        webTestClient.get()
                .uri("/v1/music/{id}","abc")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Music.class);
    }

    @Test
    public void retrieveMusicById_404() {

        stubFor(get(urlEqualTo("/v1/musicInfo/"+"abc"))
                .willReturn(aResponse()
                        .withStatus(404)
                ));

        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withHeader("Content-Type","application/json")
                        .withBodyFile("reviews.json")
                ));

        webTestClient.get()
                .uri("/v1/music/{id}","abc")
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("Music info not available for abc");
    }

    @Test
    public void retrieveMusicById_reviews_404() {

        stubFor(get(urlEqualTo("/v1/musicInfo/"+"abc"))
                .willReturn(aResponse()
                        .withHeader("Content-Type","application/json")
                        .withBodyFile("musicinfo.json")
                ));

        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withStatus(404)
                ));

        webTestClient.get()
                .uri("/v1/music/{id}","abc")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Music.class);
    }

    @Test
    public void retrieveMusicById_500() {

        stubFor(get(urlEqualTo("/v1/musicInfo/"+"abc"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("MusicInfo Service is not available")
                ));

        webTestClient.get()
                .uri("/v1/music/{id}","abc")
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Internal Server Error while calling music info");

        verify(4,getRequestedFor(urlPathEqualTo("/v1/musicInfo/abc")));
    }
}