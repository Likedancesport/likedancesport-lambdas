package com.likedancesport.service.impl;

import com.likedancesport.common.dao.ITaggableMediaResourceDao;
import com.likedancesport.common.model.domain.impl.TaggableMediaResource;
import com.likedancesport.service.ITagService;

public abstract class TaggableMediaResourceService<T extends TaggableMediaResource> extends BaseMediaResourceService<T> {
    protected final ITagService tagService;

    public TaggableMediaResourceService(ITaggableMediaResourceDao<T> serviceDomainDao, ITagService tagService) {
        super(serviceDomainDao);
        this.tagService = tagService;
    }
}