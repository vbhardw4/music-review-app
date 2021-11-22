package com.reactivespring.repository;

import com.reactivespring.domain.MusicInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MusicInfoRepository extends ReactiveMongoRepository<MusicInfo,String> {
}
