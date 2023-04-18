package com.likedancesport.service.impl;

import com.likedancesport.common.model.impl.TaggableMediaResource;
import com.likedancesport.dao.ITaggableMediaResourceDao;
import com.likedancesport.service.ITagService;

public abstract class TaggableMediaResourceService<T extends TaggableMediaResource> extends BaseMediaResourceService<T> {
    protected final ITagService tagService;

    public TaggableMediaResourceService(ITaggableMediaResourceDao<T> serviceDomainDao, ITagService tagService) {
        super(serviceDomainDao);
        this.tagService = tagService;
    }
}