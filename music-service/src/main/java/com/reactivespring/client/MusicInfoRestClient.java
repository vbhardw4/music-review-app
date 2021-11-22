package com.reactivespring.client;

import com.reactivespring.domain.MusicInfo;
import com.reactivespring.exceptions.MusicInfoClientException;
import com.reactivespring.exceptions.MusicInfoServerException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@Slf4j
public class MusicInfoRestClient {

    private WebClient webClient;
    @Value("${restClient.musicInfoUrl}")
    private String musicInfoUrl;
    public MusicInfoRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<MusicInfo> retrieveMusicInfo(String musicId) {
        var url = musicInfoUrl.concat("/{id}");

        return webClient.get()
                .uri(url,musicId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,clientResponse -> {
                    if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new MusicInfoClientException("Music info not available for "+musicId,
                                clientResponse.statusCode().value()));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(s -> {
                                return Mono.error(new MusicInfoClientException(s,clientResponse.statusCode().value()));
                            });
                })
                .onStatus(HttpStatus::is5xxServerError,clientResponse -> {
                    if(clientResponse.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                        log.error("5XX server error reported  ");
                        return Mono.error(new MusicInfoServerException("Internal Server Error while calling music info"));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(s->{
                               return Mono.error(new MusicInfoServerException(s));
                            });
                })
                .bodyToMono(MusicInfo.class)
//                .retry(3)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .log();
    }
}
