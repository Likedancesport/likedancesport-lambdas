package com.likedancesport.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LecturerUpdateRequest {
    private final String name;
    private final String description;
}
