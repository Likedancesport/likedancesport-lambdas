package com.likedancesport.common.model.domain.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity(name = "Tag")
@Table(name = "tag")
@NoArgsConstructor
public class Tag implements Serializable {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false, name = "tag_id")
    private Long id;

    @Column(nullable = false, updatable = false, name = "title", unique = true)
    private String title;

    @Cascade({
            CascadeType.SAVE_UPDATE,
            CascadeType.MERGE,
    })
    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    @JsonBackReference
    private Set<TaggableMediaResource> taggedResourses;

    public void removeResource(TaggableMediaResource taggableMediaResource) {
        taggedResourses.remove(taggableMediaResource);
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
            return false;
        }
        Tag tag = (Tag) o;
        return id != null && Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
