package com.likedancesport.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SectionUpdateRequest extends BaseUpdateRequest {
    /**
     * Used for specifying order of videos in section
     * ID of Video is mapped to its order (ID -> Order)
     */
    @JsonDeserialize(as = HashMap.class)
    private Map<Long, Integer> videoOrderingMap;
}
