package com.likedancesport.service.impl;

import com.likedancesport.common.dao.ICourseDao;
import com.likedancesport.common.dao.ISectionDao;
import com.likedancesport.common.model.domain.learning.Course;
import com.likedancesport.common.model.domain.learning.MediaResource;
import com.likedancesport.common.model.domain.learning.Section;
import com.likedancesport.request.SectionUpdateRequest;
import com.likedancesport.service.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SectionService extends BaseMediaResourceService<Section> implements ISectionService {
    private final ICourseDao courseDao;

    @Autowired
    public SectionService(ISectionDao sectionDao, ICourseDao courseDao) {
        super(sectionDao);
        this.courseDao = courseDao;
    }

    @Override
    @Transactional
    public Section createSection(Long courseId, Section section) {
        Course course = courseDao.findById(courseId).orElseThrow();
        course.addSection(section);
        return serviceDomainDao.save(section);
    }

    @Override
    public Section updateSection(Long sectionId, SectionUpdateRequest updateRequest) {
        Section section = serviceDomainDao.findById(sectionId).orElseThrow();
        section.setTitle(updateRequest.getTitle());
        section.setDescription(updateRequest.getDescription());
        section.reorderVideos(updateRequest.getVideoOrderingMap());
        return serviceDomainDao.save(section);
    }
}
