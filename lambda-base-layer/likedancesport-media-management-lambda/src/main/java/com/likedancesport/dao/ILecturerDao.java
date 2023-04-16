package com.likedancesport.dao;

import com.likedancesport.common.model.impl.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILecturerDao extends JpaRepository<Lecturer, Long> {
}
