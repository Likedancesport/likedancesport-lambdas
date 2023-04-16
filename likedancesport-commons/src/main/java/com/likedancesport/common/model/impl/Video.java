package com.likedancesport.common.model.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.likedancesport.common.enums.VideoStatus;
import com.likedancesport.common.model.IOrderableEntity;
import com.likedancesport.common.utils.misc.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity(name = "Video")
@Table(name = "video")
@NoArgsConstructor
public class Video extends TaggableMediaResource implements IOrderableEntity {
    @Column(nullable = false, name = "order_in_section")
    private Integer orderInSection;

    @Column(name = "duration_seconds", nullable = false)
    @NotNull(message = "Video duration must be specified")
    @Min(value = 1, message = "Duration must be at least one second")
    private Long durationSeconds;

    @Column(name = "views_count", nullable = false)
    @Builder.Default
    private Long viewsCount = 0L;

    @Column(name = "s3_key", nullable = false, unique = true, updatable = false)
    @NotBlank(message = "Video key is mandatory")
    private String videoS3Key;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    @JsonBackReference
    private Section section;

    @Column
    @Enumerated(EnumType.STRING)
    private VideoStatus status;

    public void setVideoS3Key(String s3Key) {
        this.videoS3Key = s3Key;
    }

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
