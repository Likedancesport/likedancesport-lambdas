package com.likedancesport.model;

import com.likedancesport.model.impl.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Builder
@Data
public class CreateVideoRequest {
    @NotBlank(message = "Video title must be specified")
    private final String title;

    @NotBlank(message = "Description must be specified")
    private final String description;

    @NotNull(message = "Video duration must be specified")
    @Min(value = 1, message = "Duration must be at least one second")
    private final Long durationSeconds;

    private final Set<Tag> tags;
}
