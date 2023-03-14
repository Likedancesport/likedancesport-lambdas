package com.likedancesport.common.model;

import com.likedancesport.common.model.impl.MediaResource;

public interface IContainerableMediaResource<T extends MediaResource> {
    void setParent(IMediaResourceContainer<T> container);
}
