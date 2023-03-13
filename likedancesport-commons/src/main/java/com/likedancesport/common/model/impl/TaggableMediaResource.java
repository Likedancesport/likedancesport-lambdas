package com.likedancesport.common.model.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
public abstract class TaggableMediaResource extends MediaResource {
    @Column(name = "tags")
    @Cascade({ CascadeType.SAVE_UPDATE})
    @ManyToMany
    @JoinTable(
            name = "entity_tag",
            joinColumns = @JoinColumn(name = "entity_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
