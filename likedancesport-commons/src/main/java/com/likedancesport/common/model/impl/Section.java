package com.likedancesport.common.model.impl;

import com.likedancesport.common.model.IMediaResourceContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity(name = "section")
@Table(name = "section")
@NoArgsConstructor
public class Section extends MediaResource implements IMediaResourceContainer<Video> {
    @Embedded
    private MediaResourceEmbeddable<Video> mediaResourceEmbeddable;
}
