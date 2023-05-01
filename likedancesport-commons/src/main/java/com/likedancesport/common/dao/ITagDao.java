package com.likedancesport.common.dao;

import com.likedancesport.common.model.domain.learning.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITagDao extends JpaRepository<Tag, Long> {
}
