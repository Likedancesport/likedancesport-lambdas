package com.likedancesport.dao;

import com.likedancesport.common.model.impl.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICourseDao extends JpaRepository<Course, Long> {
}
