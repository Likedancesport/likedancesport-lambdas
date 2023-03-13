package com.likedancesport.common.model.impl;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.likedancesport.common.model.IMediaResourceContainer;
import com.likedancesport.common.model.S3Storable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@SuperBuilder
@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public abstract class MediaResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, name = "video_id")
    private Long id;

    @Column(nullable = false, unique = true, name = "title")
    @NotBlank(message = "Video title must be specified")
    private String title;

    @Column(nullable = false, name = "description")
    @NotBlank(message = "Description must be specified")
    private String description;

    @ManyToOne
    private IMediaResourceContainer<? extends MediaResource> parent;
}
