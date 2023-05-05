package com.likedancesport.service.impl;

import com.likedancesport.common.dao.ISectionDao;
import com.likedancesport.common.dao.IVideoDao;
import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.model.domain.learning.MediaResource;
import com.likedancesport.common.model.domain.learning.Section;
import com.likedancesport.common.model.domain.learning.Video;
import com.likedancesport.request.VideoUpdateRequest;
import com.likedancesport.service.ITagService;
import com.likedancesport.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService extends TaggableMediaResourceService<Video> implements IVideoService {
    private final ISectionDao sectionDao;

    @Autowired
    public VideoService(IVideoDao videoDao, ITagService tagService, ISectionDao sectionDao) {
        super(videoDao, tagService);
        this.sectionDao = sectionDao;
    }

    @Override
    public Video createVideo(Long sectionId, Video video) {
        video.setStatus(VideoStatus.UPLOADING);
        tagService.persistAndReplaceTransientTagsIn(video);
        Section section = sectionDao.findById(sectionId).orElseThrow();
        section.addVideo(video);
        return serviceDomainDao.save(video);
    }

    @Override
    public Video updateVideo(Long id, VideoUpdateRequest updateRequest) {
        Video video = serviceDomainDao.findById(id).orElseThrow();
        video.setTitle(updateRequest.getTitle());
        video.setDescription(updateRequest.getDescription());
        tagService.changeTags(video, updateRequest.getTags());
        return serviceDomainDao.save(video);
    }
}
