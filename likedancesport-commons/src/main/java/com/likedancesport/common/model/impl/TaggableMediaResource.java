package com.likedancesport.common.model.impl;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@Entity(name = "TaggableMediaResource")
@Table(name = "taggable_media_resource")
@NoArgsConstructor
@SuperBuilder
public abstract class TaggableMediaResource extends MediaResource {
    @Cascade({
            CascadeType.SAVE_UPDATE,
            CascadeType.MERGE,
    })
    @ManyToMany
    @JoinTable(
            name = "entity_tag",
            joinColumns = @JoinColumn(name = "entity_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIdentityReference
    private Set<Tag> tags;

    /**
     * Used to dissociate tag and resource
     *
     * @param tag tag to dissociate
     */
    @Transactional
    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.removeResource(this);
    }

    /**
     * Runs before resource removal
     * Dissociates entity and all its tags
     */
    @Override
    public void remove() {
        tags.forEach(this::removeTag);
    }
}
