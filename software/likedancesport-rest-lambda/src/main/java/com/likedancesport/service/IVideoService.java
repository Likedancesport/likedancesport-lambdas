package com.likedancesport.service;

import com.likedancesport.model.impl.Tag;
import com.likedancesport.model.impl.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IVideoService {
    List<Video> getAllVideos();

    Page<Video> getVideosPaginated(Pageable pageable);

    Page<Video> getVideosByTags(Set<Tag> tags, Pageable pageable);

    Video getVideoForWatching(Long id);

    void markVideoWatched(Long id);
}
