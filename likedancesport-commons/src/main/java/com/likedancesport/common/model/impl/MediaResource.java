package com.likedancesport.common.model.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Entity(name = "MediaResource")
@Table(name = "media_resource")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MediaResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "title")
    @NotBlank(message = "Title must be specified")
    private String title;

    @Column(nullable = false, name = "description")
    @NotBlank(message = "Description must be specified")
    private String description;

    @Column(nullable = false, name = "preview_photo_s3_key", unique = true, updatable = false)
    private String previewPhotoS3Key;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        MediaResource that = (MediaResource) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @PreRemove
    @Transactional
    public abstract void remove();

    public static String generatePreviewPhotoS3Key() {
        return UUID.randomUUID().toString() + "-preview";
    }
}