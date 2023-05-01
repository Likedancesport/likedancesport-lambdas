package com.likedancesport.common.model.domain.learning;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.likedancesport.common.model.domain.IPreviewable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Table(name = "lecturer")
@NoArgsConstructor
@Entity(name = "Lecturer")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Lecturer implements IPreviewable {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String photoS3Key;

    @ManyToMany
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DETACH, CascadeType.LOCK, CascadeType.DELETE})
    @JoinTable(
            name = "course_lecturer",
            joinColumns = @JoinColumn(name = "lecturer_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @ToString.Exclude
    @JsonIdentityReference
    private Set<Course> courses;

    public Set<Course> getCourses() {
        return courses.stream().collect(Collectors.toUnmodifiableSet());
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    @Transactional
    public void removeCourse(Course course) {
        courses.remove(course);
        if (course.getLecturers().contains(this)) {
            course.removeLecturer(this);
        }
    }

    @PreRemove
    public void remove() {
        courses.forEach(this::removeCourse);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return true;
        }
        Lecturer lecturer = (Lecturer) o;
        return id != null && Objects.equals(id, lecturer.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
