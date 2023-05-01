package com.likedancesport.common.dao;

import com.likedancesport.common.model.domain.learning.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILecturerDao extends JpaRepository<Lecturer, Long> {
}
