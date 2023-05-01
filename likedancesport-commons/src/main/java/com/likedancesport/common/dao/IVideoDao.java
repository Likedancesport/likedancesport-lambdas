package com.likedancesport.common.dao;

import com.likedancesport.common.model.domain.learning.Video;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVideoDao extends ITaggableMediaResourceDao<Video> {
    Optional<Video> findByVideoS3Key(String videoS3Key);
}
