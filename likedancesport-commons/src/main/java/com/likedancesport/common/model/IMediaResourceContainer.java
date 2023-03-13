package com.likedancesport.common.model;

import com.likedancesport.common.model.impl.MediaResource;
import com.likedancesport.common.model.impl.MediaResourceEmbeddable;

public interface IMediaResourceContainer<T extends MediaResource> {
    MediaResourceEmbeddable<T> getMediaResourceEmbeddable();
}
