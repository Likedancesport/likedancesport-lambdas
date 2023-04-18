package com.likedancesport.dao;

import com.likedancesport.common.model.impl.Video;
import org.springframework.stereotype.Component;

@Component
public interface IVideoDao extends ITaggableMediaResourceDao<Video> {
}
