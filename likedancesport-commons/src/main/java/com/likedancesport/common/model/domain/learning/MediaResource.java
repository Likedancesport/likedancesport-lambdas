package com.likedancesport.common.model.domain.learning;

import com.likedancesport.common.lifecycle.BaseS3StorableEntityListener;
import com.likedancesport.common.model.domain.IPreviewable;
import com.likedancesport.common.model.domain.S3Key;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@SuperBuilder
@Entity(name = "MediaResource")
@Table(name = "media_resource")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(BaseS3StorableEntityListener.class)
public abstract class MediaResource implements IPreviewable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "description")
    private String description;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bucketName", column = @Column(name = "preview_photo_bucket_name")),
            @AttributeOverride(name = "key", column = @Column(name = "preview_photo_key"))
    })
    private S3Key previewPhotoS3Key;

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

    @Override
    public S3Key getPhotoS3Key() {
        return previewPhotoS3Key;
    }

    @Override
    public void setPhotoS3Key(S3Key s3Key) {
        this.previewPhotoS3Key = s3Key;
    }

    public static String generatePreviewPhotoS3Key() {
        return UUID.randomUUID().toString() + "-preview";
    }
}
