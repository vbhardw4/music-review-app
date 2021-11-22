package com.reactivespring.client;

import com.reactivespring.domain.Review;
import com.reactivespring.exceptions.ReviewsClientException;
import com.reactivespring.exceptions.ReviewsServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReviewsRestClient {

    private WebClient webClient;

    public ReviewsRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Value("${restClient.reviewsUrl}")
    private String reviewsUrl;

    public Flux<Review> retrieveReviews(String musicId) {
        var url = UriComponentsBuilder.fromHttpUrl(reviewsUrl)
                .queryParam("musicInfoId",musicId)
                .buildAndExpand()
                .toString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,clientResponse -> {
                    log.error("4XX errors while retrieving reviews for the musicInfoId "+musicId);
                    if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.empty();
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(s -> {
                                return Mono.error(new ReviewsClientException(s,clientResponse.statusCode().value()));
                            });
                })
                .onStatus(HttpStatus::is5xxServerError,clientResponse -> {
                    log.error("5XX errors while retrieving reviews for the musicInfoId "+musicId);
                    if(clientResponse.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        return Mono.error(new ReviewsServerException("INTERNAL_SERVER_ERRORReviews not found for music id "));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(s -> {
                                return Mono.error(new ReviewsServerException(s));
                            });
                })
                .bodyToFlux(Review.class);
    }
}
