package com.likedancesport.common.dao;

import com.likedancesport.common.model.domain.impl.Course;
import org.springframework.stereotype.Repository;

@Repository
public interface ICourseDao extends ITaggableMediaResourceDao<Course> {
}
