package com.likedancesport.service.impl;

import com.likedancesport.common.dao.IMediaResourceDao;
import com.likedancesport.common.model.domain.learning.MediaResource;
import com.likedancesport.service.IBaseMediaResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class BaseMediaResourceService<T extends MediaResource> implements IBaseMediaResourceService<T> {
    protected final IMediaResourceDao<T> serviceDomainDao;

    public BaseMediaResourceService(IMediaResourceDao<T> serviceDomainDao) {
        this.serviceDomainDao = serviceDomainDao;
    }

    @Override
    public void delete(Long id) {
        serviceDomainDao.deleteById(id);
    }

    @Override
    public T findById(Long id) {
        return serviceDomainDao.findById(id).orElseThrow();
    }

    @Override
    public List<T> findAll() {
        return serviceDomainDao.findAll();
    }

    @Override
    public Page<T> findAllPaginated(Pageable pageable) {
        return serviceDomainDao.findAll(pageable);
    }

    @Override
    public Page<T> findByTitle(String title, Pageable pageable) {
        return serviceDomainDao.findByTitleContaining(title, pageable);
    }
}
