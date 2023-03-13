package com.likedancesport.common.model.impl;

import com.likedancesport.common.model.S3Storable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity(name = "video")
@Table(name = "videos")
@NoArgsConstructor
public class Video extends TaggableMediaResource implements S3Storable {
    @Column(name = "duration_seconds", nullable = false)
    @NotNull(message = "Video duration must be specified")
    @Min(value = 1, message = "Duration must be at least one second")
    private Long durationSeconds;

    @Column(name = "views_count", nullable = false)
    @Builder.Default
    private Long viewsCount = 0L;

    @Column(name = "s3_key", nullable = false, unique = true)
    @NotBlank(message = "Video key is mandatory")
    private String s3Key;
}
