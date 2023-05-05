package com.likedancesport.common.model.domain.learning;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.likedancesport.common.lifecycle.BaseS3StorableEntityListener;
import com.likedancesport.common.utils.collection.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Table(name = "course")
@NoArgsConstructor
@Entity(name = "Course")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Course extends TaggableMediaResource {
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.LOCK, CascadeType.DELETE})
    @OneToMany(orphanRemoval = true, mappedBy = "course")
    @ToString.Exclude
    @OrderBy("order_in_course")
    @JsonManagedReference
    private List<Section> sections;

    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.LOCK, CascadeType.DELETE})
    @ManyToMany
    @ToString.Exclude
    @OrderBy("name")
    @JsonIdentityReference
    private Set<Lecturer> lecturers;

    public List<Section> getSections() {
        return sections.stream().collect(Collectors.toUnmodifiableList());
    }

    public Set<Lecturer> getLecturers() {
        return lecturers.stream().collect(Collectors.toUnmodifiableSet());
    }

    public void addLecturer(Lecturer lecturer) {
        lecturers.add(lecturer);
        lecturer.addCourse(this);
    }

    public void addSection(Section section) {
        if (section.getOrderInCourse() == null || section.getOrderInCourse() > sections.size()) {
            section.setOrderInCourse(sections.size());
        }
        sections.add(section.getOrderInCourse(), section);
        reorderSectionsByIndex();
        section.setCourse(this);
    }

    /**
     * Reorders existing sections in course
     *
     * @param orderingMap map of Ids to Ordering Numbers of sections
     */
    public void reorderSections(Map<Long, Integer> orderingMap) {
        CollectionUtils.reorderByMap(sections, orderingMap);
    }

    @Transactional
    public void removeLecturer(Lecturer lecturer) {
        lecturers.remove(lecturer);
        if (lecturer.getCourses().contains(this)) {
            lecturer.removeCourse(this);
        }
    }

    @Override
    public void remove() {
        super.remove();
        lecturers.forEach(this::removeLecturer);
    }

    public void removeSection(Section section) {
        sections.remove(section);
        reorderSectionsByIndex();
    }

    private void reorderSectionsByIndex() {
        CollectionUtils.orderByIndex(sections);
    }
}
