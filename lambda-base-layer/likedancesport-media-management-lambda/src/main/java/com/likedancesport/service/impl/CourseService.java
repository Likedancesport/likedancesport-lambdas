package com.likedancesport.service.impl;

import com.likedancesport.common.dao.ICourseDao;
import com.likedancesport.common.dao.ILecturerDao;
import com.likedancesport.common.model.domain.learning.Course;
import com.likedancesport.common.model.domain.learning.Lecturer;
import com.likedancesport.common.model.domain.learning.MediaResource;
import com.likedancesport.request.CourseUpdateRequest;
import com.likedancesport.service.ICourseService;
import com.likedancesport.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends TaggableMediaResourceService<Course> implements ICourseService {
    private final ILecturerDao lecturerDao;

    @Autowired
    public CourseService(ICourseDao courseDao, ITagService tagService, ILecturerDao lecturerDao) {
        super(courseDao, tagService);
        this.lecturerDao = lecturerDao;
    }

    @Override
    public void addLecturerToCourse(Long courseId, Long lecturerId) {
        Lecturer lecturer = lecturerDao.findById(lecturerId).orElseThrow();
        Course course = findById(courseId);
        course.addLecturer(lecturer);
        serviceDomainDao.save(course);
    }

    @Override
    public Course createCourse(Course course) {
        course.setPreviewPhotoS3Key(MediaResource.generatePreviewPhotoS3Key());
        tagService.persistAndReplaceTransientTagsIn(course);
        return serviceDomainDao.save(course);
    }

    @Override
    public Course updateCourse(Long courseId, CourseUpdateRequest updateRequest) {
        Course course = serviceDomainDao.findById(courseId).orElseThrow();
        course.setTitle(updateRequest.getTitle());
        course.setDescription(updateRequest.getDescription());
        course.reorderSections(updateRequest.getSectionOrderingMap());
        tagService.changeTags(course, updateRequest.getTags());
        return serviceDomainDao.save(course);
    }
}
