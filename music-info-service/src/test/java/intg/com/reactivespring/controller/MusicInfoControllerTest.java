package com.reactivespring.controller;

import com.reactivespring.domain.MusicInfo;
import com.reactivespring.repository.MusicInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

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
class MusicInfoControllerTest {

    @Autowired
    MusicInfoRepository musicinfoRepository;

    @Autowired
    WebTestClient webTestClient;

    private static String MUSIC_INFO_URL = "/v1/musicInfo";
    @BeforeEach
    void setUp() {
        var musicInfoList = List.of(new MusicInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MusicInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MusicInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        /**
         * Blocking calls are only allowed in the test cases
         */
        musicinfoRepository.saveAll(musicInfoList)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        musicinfoRepository.deleteAll()
                .block();
    }

    @Test
    public void addMusicInfo() {

        var musicInfo = new MusicInfo(null, "Test music",
                2001, List.of("test cast 1", "test cast 2"), LocalDate.parse("2005-06-15"));

        webTestClient.post()
                .uri(MUSIC_INFO_URL)
                .bodyValue(musicInfo)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MusicInfo.class)
                .consumeWith(response->{
                    var savedMusicInfo = response.getResponseBody();
                    assertNotNull(savedMusicInfo.getMusicInfoId());
                });
    }

    @Test
    public void getAllMusicInfo() {

        webTestClient.get()
                .uri(MUSIC_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MusicInfo.class)
                .hasSize(3);
    }

    @Test
    public void getMusicInfoById() {

        var musicInfoId = "abc";

        webTestClient.get()
                .uri(MUSIC_INFO_URL+"/{id}",musicInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MusicInfo.class)
                .consumeWith(result -> {
                    var MusicInfo =  result.getResponseBody();
                    assertNotNull(MusicInfo);
                    assertEquals("Dark Knight Rises",MusicInfo.getName());
                });
    }

    @Test
    public void getMusicInfoById_NotFound() {

        var musicInfoId = "def";

        webTestClient.get()
                .uri(MUSIC_INFO_URL+"/{id}",musicInfoId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void updateMusicInfoById() {

        MusicInfo musicInfo = new MusicInfo("abc", "Dark Knight Rises Updated",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        webTestClient.put()
                .uri(MUSIC_INFO_URL+"/{id}","abc")
                .bodyValue(musicInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MusicInfo.class)
                .consumeWith(result -> {
                    var MusicInfo =  result.getResponseBody();
                    assertNotNull(MusicInfo);
                    assertEquals("Dark Knight Rises Updated",MusicInfo.getName());
                });
    }

    @Test
    public void updateMusicInfoById_notFound() {

        MusicInfo musicInfo = new MusicInfo("abc", "Dark Knight Rises Updated",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        webTestClient.put()
                .uri(MUSIC_INFO_URL+"/{id}","def")
                .bodyValue(musicInfo)
                .exchange()
                .expectStatus()
                .isNotFound();

    }


    @Test
    public void deleteMusicInfoById() {

        var musicInfoId = "abc";

        webTestClient.delete()
                .uri(MUSIC_INFO_URL+"/{id}",musicInfoId)
                .exchange()
                .expectStatus()
                .isNoContent();

    }

}