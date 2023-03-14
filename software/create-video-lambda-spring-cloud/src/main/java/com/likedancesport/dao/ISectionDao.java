package com.likedancesport.dao;

import com.likedancesport.common.model.impl.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISectionDao extends JpaRepository<Section, Long> {
}
