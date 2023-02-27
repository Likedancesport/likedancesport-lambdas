package com.likedancesport.dao;

import com.likedancesport.model.impl.Tag;
import com.likedancesport.model.impl.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface IVideoDao extends JpaRepository<Video, Long> {
    Page<Video> findByVideoTagsIn(Set<Tag> tags, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE videos SET views_count = views_count = 1 WHERE id = :id", nativeQuery = true)
    void incrementViews(@Param("id") Long id);
}
