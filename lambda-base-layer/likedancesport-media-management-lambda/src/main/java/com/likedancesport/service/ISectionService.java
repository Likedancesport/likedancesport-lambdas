package com.likedancesport.service;

import com.likedancesport.common.model.domain.impl.Section;
import com.likedancesport.request.SectionUpdateRequest;

public interface ISectionService extends IBaseMediaResourceService<Section> {

    Section createSection(Long courseId, Section section);

    Section updateSection(Long sectionId, SectionUpdateRequest updateRequest);
}
