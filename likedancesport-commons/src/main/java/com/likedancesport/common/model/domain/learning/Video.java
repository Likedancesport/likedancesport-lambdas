package com.likedancesport.common.model.domain.learning;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.lifecycle.VideoEntityListener;
import com.likedancesport.common.model.domain.HlsGroup;
import com.likedancesport.common.model.domain.IOrderableEntity;
import com.likedancesport.common.model.domain.S3Key;
import com.likedancesport.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity(name = "Video")
@Table(name = "video")
@NoArgsConstructor
@EntityListeners({VideoEntityListener.class})
public class Video extends TaggableMediaResource implements IOrderableEntity {
    @Column(nullable = false, name = "order_in_section")
    private Integer orderInSection;

    @Column(name = "duration_seconds")
    private Long durationSeconds;

    @Column(name = "views_count", nullable = false)
    @Builder.Default
    private Long viewsCount = 0L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bucketName", column = @Column(name = "mp4_video_bucket_name")),
            @AttributeOverride(name = "key", column = @Column(name = "mp4_video_key"))
    })
    private S3Key mp4AssetS3Key;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "hls_group_id", unique = true)
    private HlsGroup hlsGroup;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    @JsonBackReference
    private Section section;

    @Column
    @Enumerated(EnumType.STRING)
    private VideoStatus status;

    @Override
    public Integer getOrder() {
        return getOrderInSection();
    }

    @Override
    public void setOrder(Integer order) {
        setOrderInSection(order);
    }

    @Override
    public void remove() {
        super.remove();
        section.removeVideo(this);
    }

    @Transactional
    public void setSection(Section section) {
        if (this.section != null) {
            this.section.removeVideo(this);
        }
        this.section = section;
    }

    public static String generateS3Key(Video video) {
        return "videos/" + StringUtils.replaceInlineWhitespacesWith("-", video.getTitle().toLowerCase().trim());
    }
}
