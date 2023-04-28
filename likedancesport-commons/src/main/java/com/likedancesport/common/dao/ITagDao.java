package com.likedancesport.common.dao;

import com.likedancesport.common.model.domain.impl.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITagDao extends JpaRepository<Tag, Long> {
}
