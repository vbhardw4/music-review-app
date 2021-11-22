package com.reactivespring.controller;

import com.reactivespring.client.MusicInfoRestClient;
import com.reactivespring.client.ReviewsRestClient;
import com.reactivespring.domain.Music;
import com.reactivespring.service.MusicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/music")
public class MusicController {

    private MusicService musicService;
    private MusicInfoRestClient musicInfoRestClient;
    private ReviewsRestClient reviewsRestClient;

    public MusicController(MusicService musicService, MusicInfoRestClient musicInfoRestClient, ReviewsRestClient reviewsRestClient) {
        this.musicService = musicService;
        this.musicInfoRestClient = musicInfoRestClient;
        this.reviewsRestClient = reviewsRestClient;
    }

    @GetMapping("/{id}")
    public Mono<Music> retrieveMusicById(@PathVariable("id") String musicInfoId) {
        return musicInfoRestClient.retrieveMusicInfo(musicInfoId).log()
                .flatMap(musicInfo -> {
                    var reviewsListMono = reviewsRestClient.retrieveReviews(musicInfoId)
                            .collectList();

                     return reviewsListMono.map(reviews ->  new Music(musicInfo,reviews));
                });
    }
}
