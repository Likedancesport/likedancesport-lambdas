package com.likedancesport.service;

import com.likedancesport.common.model.impl.MediaResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBaseMediaResourceService<T extends MediaResource> {
    void delete(Long id);

    T findById(Long id);

    List<T> findAll();

    Page<T> findAllPaginated(Pageable pageable);

    Page<T> findByTitle(String title, Pageable pageable);
}
