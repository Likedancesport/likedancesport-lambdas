package com.likedancesport.dao;

import com.likedancesport.model.impl.Video;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public interface IVideoDao extends Repository<Video, Long> {
    @Transactional
    Video save(Video video);
}
