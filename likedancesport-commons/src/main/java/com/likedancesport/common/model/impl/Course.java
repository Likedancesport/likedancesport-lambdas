package com.likedancesport.common.model.impl;

import com.likedancesport.common.model.IMediaResourceContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Table(name = "course")
@NoArgsConstructor
@Entity(name = "course")
public class Course extends TaggableMediaResource implements IMediaResourceContainer<Section> {
    @Embedded
    private MediaResourceEmbeddable<Section> mediaResourceEmbeddable;

    @Column
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parent")
    List<Section> resources;
}
