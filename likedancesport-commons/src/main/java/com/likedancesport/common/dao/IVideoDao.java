package com.likedancesport.common.dao;

import com.likedancesport.common.model.domain.impl.Video;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVideoDao extends ITaggableMediaResourceDao<Video> {
    Optional<Video> findByVideoS3Key(String videoS3Key);
}
