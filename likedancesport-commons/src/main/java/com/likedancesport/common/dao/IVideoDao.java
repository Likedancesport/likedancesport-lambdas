package com.likedancesport.common.dao;

import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.model.domain.learning.Video;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVideoDao extends ITaggableMediaResourceDao<Video> {
    @Query("SELECT v FROM Video v WHERE v.mp4AssetS3Key = :mp4AssetS3Key")
    Optional<Video> findByMp4AssetS3Key(@Param("mp4AssetS3Key") S3Key mp4AssetS3Key);

}
