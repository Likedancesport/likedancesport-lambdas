package com.likedancesport.service;


import com.likedancesport.common.model.impl.Video;
import com.likedancesport.request.VideoUpdateRequest;

public interface IVideoService extends ITaggableMediaResourseService<Video> {
    Video createVideo(Long sectionId, Video video);

    Video updateVideo(Long videoId, VideoUpdateRequest updateRequest);
}
