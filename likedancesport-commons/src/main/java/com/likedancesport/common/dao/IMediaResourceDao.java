package com.likedancesport.common.dao;

import com.likedancesport.common.model.domain.learning.MediaResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMediaResourceDao<T extends MediaResource> extends JpaRepository<T, Long> {
    Page<T> findByTitleContaining(String title, Pageable pageable);
}
