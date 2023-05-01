package com.likedancesport.request;

import com.likedancesport.common.model.domain.learning.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public abstract class TaggableMediaResourceUpdateRequest extends BaseUpdateRequest {
    private Set<Tag> tags;
}
