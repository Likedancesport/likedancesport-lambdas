package com.likedancesport.dao;

import com.likedancesport.common.model.impl.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVideoDao extends CrudRepository<Video, Long> {
    Optional<Video> findByVideoS3Key(String videoS3Key);
}
