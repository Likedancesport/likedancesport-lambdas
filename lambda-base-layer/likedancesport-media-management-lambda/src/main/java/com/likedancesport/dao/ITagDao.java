package com.likedancesport.dao;

import com.likedancesport.common.model.impl.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITagDao extends JpaRepository<Tag, Long> {
}
