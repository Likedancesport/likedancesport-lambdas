package com.likedancesport.common.model.domain.marketplace;

import com.likedancesport.common.model.domain.S3Key;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity(name = "MarketplaceVideo")
@Table(name = "marketplace_video")
@NoArgsConstructor
public class MarketplaceVideo extends MarketplaceMediaResource {
    @Column(name = "thumbnail_video_s3_key")
    private S3Key thumbnailVideoS3Key;

    @Override
    public String getPrefix() {
        return "videos";
    }
}
