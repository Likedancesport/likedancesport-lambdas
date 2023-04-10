package com.likedancesport.dao;

import com.likedancesport.common.model.impl.Course;
import org.springframework.stereotype.Repository;

@Repository
public interface ICourseDao extends ITaggableMediaResourceDao<Course> {
}
