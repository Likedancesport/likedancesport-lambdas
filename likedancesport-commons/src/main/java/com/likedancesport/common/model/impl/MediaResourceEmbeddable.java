package com.likedancesport.common.model.impl;

import com.likedancesport.common.model.IMediaResourceContainer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class MediaResourceEmbeddable<T extends MediaResource> {
    @Column
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    List<T> resources;

    public void addResource(T resource, IMediaResourceContainer<T> parent) {
        resource.setParent(parent);
        resources.add(resource);
    }
}
