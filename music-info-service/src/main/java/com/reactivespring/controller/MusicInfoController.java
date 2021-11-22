package com.reactivespring.controller;

import com.reactivespring.domain.MusicInfo;
import com.reactivespring.service.MusicInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class MusicInfoController {

    private MusicInfoService musicInfoService;

    public MusicInfoController(MusicInfoService musicInfoService) {
        this.musicInfoService = musicInfoService;
    }

    @PostMapping("/musicInfo")
    @ResponseStatus(HttpStatus.OK)
    public Mono<MusicInfo> addMusicInfo(@RequestBody @Valid MusicInfo musicInfo) {
        var musicMono =  musicInfoService.addMusicInfo(musicInfo);
        return musicMono;
    }

    @GetMapping("/musicInfo")
    public Flux<MusicInfo> getAllMusicInfo() {
        return musicInfoService.getAllMusicInfos();
    }

    @GetMapping("/musicInfo/{id}")
    public Mono<ResponseEntity<MusicInfo>> getAllMusicInfoById(@PathVariable String id) {
        return musicInfoService.getAllMusicInfoById(id)
                .map(musicInfo -> {
                    return ResponseEntity.ok().body(musicInfo);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PutMapping("/musicInfo/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<MusicInfo>> updateMusicInfoById(@RequestBody MusicInfo musicInfo, @PathVariable String id) {
        return musicInfoService.updateMusicInfoById(musicInfo,id)
                .map(info -> {
                    return ResponseEntity.ok().body(info);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();

    }

    @DeleteMapping("/musicInfo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMusicInfoById(@PathVariable String id) {
        return musicInfoService.deleteMusicById(id);
    }

}
