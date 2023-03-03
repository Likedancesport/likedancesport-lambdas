package com.likedancesport.common.model.impl;

import com.likedancesport.common.model.Model;
import com.likedancesport.common.model.S3Storable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity(name = "video")
@Table(name = "videos")
@NoArgsConstructor
public class Video implements Model, S3Storable {
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

    @Column(name = "s3_key", nullable = false, unique = true)
    @NotBlank(message = "Video key is mandatory")
    private String s3Key;

    @Column(name = "duration_seconds", nullable = false)
    @NotNull(message = "Video duration must be specified")
    @Min(value = 1, message = "Duration must be at least one second")
    private Long durationSeconds;

    @Column(name = "views_count", nullable = false)
    @Builder.Default
    private Long viewsCount = 0L;

    @Column(name = "tags")
    @ManyToMany
    @Cascade({CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.SAVE_UPDATE, CascadeType.REMOVE})
    @JoinTable(
            name = "video_tag",
            joinColumns = @JoinColumn(name = "video_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ToString.Exclude
    private Set<Tag> videoTags;

    @Override
    public Long getEntityId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Video video = (Video) o;
        return id != null && Objects.equals(id, video.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
