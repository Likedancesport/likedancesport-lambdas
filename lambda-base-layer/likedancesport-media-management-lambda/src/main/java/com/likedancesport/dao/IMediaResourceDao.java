package com.likedancesport.dao;

import com.likedancesport.common.model.impl.MediaResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMediaResourceDao<T extends MediaResource> extends JpaRepository<T, Long> {
    Page<T> findByTitleContaining(String title, Pageable pageable);
}
