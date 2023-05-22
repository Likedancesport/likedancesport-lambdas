package com.likedancesport.common.model.domain.learning;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.likedancesport.common.model.domain.IOrderableEntity;
import com.likedancesport.common.utils.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity(name = "Section")
@Table(name = "section")
@NoArgsConstructor
public class Section extends MediaResource implements IOrderableEntity {

    @Column(nullable = false, name = "order_in_course")
    private Integer orderInCourse;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private Course course;

    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.LOCK, CascadeType.DELETE})
    @OneToMany(orphanRemoval = true, mappedBy = "section")
    @ToString.Exclude
    @OrderBy("order_in_section")
    private List<Video> videos;

    @Override
    public Integer getOrder() {
        return getOrderInCourse();
    }

    @Override
    public void setOrder(Integer order) {
        setOrderInCourse(order);
    }

    public List<Video> getVideos() {
        return videos.stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void remove() {
        course.removeSection(this);
    }

    /**
     * adds video and reorders videos according to their index
     * associates video with this section
     *
     * @param video
     */
    @Transactional
    public void addVideo(Video video) {
        if (video.getOrderInSection() == null || video.getOrderInSection() > videos.size()) {
            video.setOrderInSection(videos.size());
        }
        videos.add(video.getOrderInSection(), video);
        reorderVideosByIndex();
        video.setSection(this);
    }

    /**
     * Reorders existing videos in section according to specified map
     *
     * @param orderingMap map of Ids to Ordering Numbers of videos
     */
    public void reorderVideos(Map<Long, Integer> orderingMap) {
        CollectionUtils.reorderByMap(videos, orderingMap);
    }

    /**
     * Dissociates video from section and reorders videos by its index.
     * If you call this method you must associate new section to video
     * or completely delete video
     *
     * @param video video to remove
     */
    public void removeVideo(Video video) {
        videos.remove(video);
        reorderVideosByIndex();
    }

    private void reorderVideosByIndex() {
        CollectionUtils.orderByIndex(videos);
    }
}
