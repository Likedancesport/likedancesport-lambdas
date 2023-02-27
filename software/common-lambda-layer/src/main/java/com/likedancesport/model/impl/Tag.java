package com.likedancesport.model.impl;

import com.likedancesport.model.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity(name = "tag")
@Table(name = "tags")
@NoArgsConstructor
public class Tag implements Model {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false, name = "tag_id")
    private Long id;

    @Column(nullable = false, updatable = false, name = "title", unique = true)
    private String title;

    @ManyToMany(mappedBy = "videoTags", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @ToString.Exclude
    private Set<Video> videos;

    @Override
    public Long getEntityId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Tag tag = (Tag) o;
        return id != null && Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
