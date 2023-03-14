package com.likedancesport.dao;

import com.likedancesport.common.model.impl.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public interface IVideoDao extends JpaRepository<Video, Long> {
}
