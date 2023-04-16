package com.likedancesport.common.dto.full;

import com.likedancesport.common.model.impl.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TaggableMediaResourceDto extends BaseMediaResourceDto {
    private final Set<Tag> tags;
}
