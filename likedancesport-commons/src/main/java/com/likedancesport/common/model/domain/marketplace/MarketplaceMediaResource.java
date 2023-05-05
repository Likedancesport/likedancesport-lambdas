package com.likedancesport.common.model.domain.marketplace;

import com.likedancesport.common.model.domain.IPreviewable;
import com.likedancesport.common.model.domain.S3Key;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class MarketplaceMediaResource implements IPreviewable {
    @Id
    private Long id;

    @JoinColumn(name = "participant_id")
    @ManyToOne
    private Participant participant;

    @Embedded
    private Price price;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bucketName", column = @Column(name = "preview_photo_bucket_name")),
            @AttributeOverride(name = "key", column = @Column(name = "preview_photo_key"))
    })
    private S3Key previewPhotoS3Key;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bucketName", column = @Column(name = "resource_bucket_name")),
            @AttributeOverride(name = "key", column = @Column(name = "resource_key"))
    })
    private S3Key resourceS3Key;

    @Override
    public S3Key getPhotoS3Key() {
        return previewPhotoS3Key;
    }

    @Override
    public void setPhotoS3Key(S3Key s3Key) {
        this.previewPhotoS3Key = s3Key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MarketplaceMediaResource that = (MarketplaceMediaResource) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public abstract String getPrefix();

    @PreRemove
    public void remove() {
        participant.removeResource(this);
    }
}
