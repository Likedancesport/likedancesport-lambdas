package com.likedancesport.service.impl;

import com.likedancesport.common.model.impl.Tag;
import com.likedancesport.common.model.impl.TaggableMediaResource;
import com.likedancesport.dao.ITagDao;
import com.likedancesport.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService implements ITagService {
    private final ITagDao tagDao;

    @Autowired
    public TagService(ITagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public void persistAndReplaceTransientTagsIn(TaggableMediaResource taggableMediaResource) {
        Set<Tag> persistedTags = persistTags(taggableMediaResource.getTags());
        taggableMediaResource.setTags(persistedTags);
    }

    private Set<Tag> persistTags(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> (tag.getId() == null || tagDao.exists(Example.of(tag))) ? tagDao.save(tag) : tag)
                .collect(Collectors.toSet());
    }

    @Override
    public void changeTags(TaggableMediaResource taggableMediaResource, Set<Tag> tags) {
        Set<Tag> resourceTags = taggableMediaResource.getTags();
        resourceTags.stream().filter(tag -> !tags.contains(tag)).forEach(taggableMediaResource::removeTag);
        Set<Tag> persistedTags = persistTags(resourceTags);
        taggableMediaResource.setTags(persistedTags);
    }
}
