package com.reactivespring.controller;

import com.reactivespring.domain.MusicInfo;
import com.reactivespring.service.MusicInfoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;

@WebFluxTest(controllers = MusicInfoController.class)
@AutoConfigureWebTestClient

public class MusicInfoControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MusicInfoService musicInfoServiceMock;

    private static String MUSICS_INFO_URL = "/v1/musicInfo";

    @Test
    void getAllMusicsInfo() {

        var musicinfos = List.of(new MusicInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MusicInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MusicInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        Mockito.when(musicInfoServiceMock.getAllMusicInfos())
                        .thenReturn(Flux.fromIterable(musicinfos));

        webTestClient.get()
                .uri(MUSICS_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MusicInfo.class)
                .hasSize(3);
    }

    @Test
    void getMusicInfoById() {
        var musicinfoMono = new MusicInfo("abcd", "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        Mockito.when(musicInfoServiceMock.getAllMusicInfoById(isA(String.class)))
                .thenReturn(Mono.just(musicinfoMono));

        webTestClient.get()
                .uri(MUSICS_INFO_URL+"/{id}", "abcd")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MusicInfo.class)
                .consumeWith(musicInfoEntityExchangeResult -> {
                    var musicInfo = musicInfoEntityExchangeResult.getResponseBody();
                    assertEquals("abcd",musicInfo.getMusicInfoId());
                    assertEquals("Batman Begins",musicInfo.getName());
                });

    }


    @Test
    void updateMusicInfoById() {
        var musicInfoMono = new MusicInfo("abcd", "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        Mockito.when(musicInfoServiceMock.updateMusicInfoById(isA(MusicInfo.class),isA(String.class)))
                .thenReturn(Mono.just(musicInfoMono));

        webTestClient.put()
                .uri(MUSICS_INFO_URL+"/{id}", "abcd")
                .bodyValue(musicInfoMono)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MusicInfo.class)
                .consumeWith(musicInfoEntityExchangeResult -> {
                    var musicInfo = musicInfoEntityExchangeResult.getResponseBody();
                    assertEquals("abcd",musicInfo.getMusicInfoId());
                    assertEquals("Batman Begins",musicInfo.getName());
                });

    }

    @Test
    void addMusicInfo() {
        var musicInfoMono = new MusicInfo("abcd", "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        Mockito.when(musicInfoServiceMock.addMusicInfo(isA(MusicInfo.class)))
                .thenReturn(Mono.just(musicInfoMono));

        webTestClient.post()
                .uri(MUSICS_INFO_URL)
                .bodyValue(musicInfoMono)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MusicInfo.class)
                .consumeWith(musicInfoEntityExchangeResult -> {
                    var musicInfo = musicInfoEntityExchangeResult.getResponseBody();
                    assertEquals("abcd",musicInfo.getMusicInfoId());
                    assertEquals("Batman Begins",musicInfo.getName());
                });

    }

    @Test
    void deleteMusicInfoById() {

        Mockito.when(musicInfoServiceMock.deleteMusicById(isA(String.class)))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(MUSICS_INFO_URL+"/{id}","abc")
                .exchange()
                .expectStatus()
                .isNoContent();

    }

    @Test
    void addMusicInfo_withValidation() {
        var musicInfoMono = new MusicInfo("abcd", "",
                -2005, List.of(""), LocalDate.parse("2005-06-15"));

        Mockito.when(musicInfoServiceMock.addMusicInfo(isA(MusicInfo.class)))
                .thenReturn(Mono.just(musicInfoMono));

        webTestClient.post()
                .uri(MUSICS_INFO_URL)
                .bodyValue(musicInfoMono)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                   var responseBody = stringEntityExchangeResult.getResponseBody();
                   assertNotNull(responseBody);
                   assertEquals("MusicInfo.Cast must be present,MusicInfo.name must be present,MusicInfo.year must be present",responseBody);
                });

    }

}
