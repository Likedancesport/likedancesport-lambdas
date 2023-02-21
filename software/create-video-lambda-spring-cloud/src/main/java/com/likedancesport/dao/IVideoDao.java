package com.likedancesport.dao;

import com.likedancesport.model.impl.Video;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface IVideoDao extends Repository<Video, Long> {
    Video save(Video video);
}
