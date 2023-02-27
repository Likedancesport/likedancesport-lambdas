package com.likedancesport.service.impl;

import com.likedancesport.dao.IVideoDao;
import com.likedancesport.model.impl.Tag;
import com.likedancesport.model.impl.Video;
import com.likedancesport.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class VideoService implements IVideoService {
    private final IVideoDao videoDao;

    @Autowired
    public VideoService(IVideoDao videoDao) {
        this.videoDao = videoDao;
    }

    @Override
    public List<Video> getAllVideos() {
        return videoDao.findAll();
    }

    @Override
    public Page<Video> getVideosPaginated(Pageable pageable) {
        return videoDao.findAll(pageable);
    }

    @Override
    public Page<Video> getVideosByTags(Set<Tag> tags, Pageable pageable) {
        return tags.size() == 0
                ? getVideosPaginated(pageable)
                : videoDao.findByVideoTagsIn(tags, pageable);
    }

    @Override
    public Video getVideoForWatching(Long id) {
        Optional<Video> optionalVideo = videoDao.findById(id);
        if(optionalVideo.isEmpty()) {
            throw new RuntimeException("Not Found");
        }
        return optionalVideo.get();
    }

    @Override
    public void markVideoWatched(Long id) {
        videoDao.incrementViews(id);
    }
}
