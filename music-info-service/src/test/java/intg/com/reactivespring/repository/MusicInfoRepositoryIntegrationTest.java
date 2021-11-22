package com.reactivespring.repository;

import com.reactivespring.domain.MusicInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

@DataMongoTest
@ActiveProfiles("test")
class MusicInfoRepositoryIntegrationTest {

    @Autowired
    MusicInfoRepository musicInfoRepository;

    @BeforeEach
    void setup() {
        var musicinfos = List.of(new MusicInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MusicInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MusicInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        /**
         * Blocking calls are only allowed in the test cases
         */
        musicInfoRepository.saveAll(musicinfos)
                .blockLast();
    }

    @AfterEach
    void deleteAll() {
        musicInfoRepository.deleteAll()
                .block();
    }

    @Test
    void findAll(){
        var musicInfoFlux = musicInfoRepository.findAll().log();

        StepVerifier.create(musicInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void findById() {
        var musicInfoMono = musicInfoRepository.findById("abc");

        StepVerifier.create(musicInfoMono)
                .assertNext(MusicInfo -> {
                    assertEquals("Dark Knight Rises",MusicInfo.getName());
                });
    }

    @Test
    public void saveMusicInfo() {

        MusicInfo MusicInfo = new MusicInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale1", "Michael Cane1"), LocalDate.parse("2005-06-15"));

        var musicInfoMono = musicInfoRepository.save(MusicInfo)
                                                                .log();

        StepVerifier.create(musicInfoMono)
                .assertNext(musicInfo -> {
                    assertNotNull(musicInfo.getMusicInfoId());
                    assertEquals("Dark Knight Rises",musicInfo.getName());
                });
    }

    @Test
    public void deleteMusicInfo() {

        var musicInfoMono = musicInfoRepository.deleteById("abc").block();

        var musicinfoFlux =  musicInfoRepository.findAll();

        StepVerifier.create(musicinfoFlux)
                .expectNextCount(2)
                .verifyComplete();

    }

}