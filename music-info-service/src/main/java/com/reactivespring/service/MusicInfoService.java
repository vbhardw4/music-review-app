package com.reactivespring.service;

import com.reactivespring.domain.MusicInfo;
import com.reactivespring.repository.MusicInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MusicInfoService {

    private MusicInfoRepository musicInfoRepository;

    public MusicInfoService(MusicInfoRepository musicInfoRepository) {
        this.musicInfoRepository = musicInfoRepository;
    }

    public Mono<MusicInfo> addMusicInfo(MusicInfo musicinfo) {
        return musicInfoRepository.save(musicinfo);
    }

    public Flux<MusicInfo> getAllMusicInfos() {
        return musicInfoRepository.findAll();
    }

    public Mono<MusicInfo> getAllMusicInfoById(String musicInfoId) {
        return musicInfoRepository.findById(musicInfoId);
    }

    public Mono<MusicInfo> updateMusicInfoById(MusicInfo updatedMusicInfo, String id) {
        return musicInfoRepository.findById(id)
                .flatMap(musicInfo -> {
                    musicInfo.setCast(updatedMusicInfo.getCast());
                    musicInfo.setName(updatedMusicInfo.getName());
                    musicInfo.setYear(updatedMusicInfo.getYear());
                    musicInfo.setReleaseDate(updatedMusicInfo.getReleaseDate());
                    musicInfo.setMusicInfoId(updatedMusicInfo.getMusicInfoId());

                    return musicInfoRepository.save(musicInfo);
                });
    }

    public Mono<Void> deleteMusicById(String id) {
        return musicInfoRepository.deleteById(id);
    }
}
