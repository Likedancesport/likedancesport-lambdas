package com.likedancesport.service;

import com.likedancesport.common.model.domain.impl.Tag;
import com.likedancesport.common.model.domain.impl.TaggableMediaResource;

import java.util.Set;

public interface ITagService {
    void persistAndReplaceTransientTagsIn(TaggableMediaResource taggableMediaResource);

    void changeTags(TaggableMediaResource taggableMediaResource, Set<Tag> tags);
}
