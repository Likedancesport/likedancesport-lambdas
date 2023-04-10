package com.likedancesport.service;

import com.likedancesport.common.model.impl.MediaResource;
import com.likedancesport.dao.IMediaResourceDao;
import com.likedancesport.service.impl.BaseMediaResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class BaseMediaResourceServiceTest<T extends MediaResource> {
    protected BaseMediaResourceService<T> mediaResourceService;
    private final IMediaResourceDao<T> mediaResourceDao = mock(IMediaResourceDao.class);
    private final T notPersistedEntity;
    private final T persistentEntity;

    public BaseMediaResourceServiceTest(T notPersistedEntity, T persistentEntity) {
        this.notPersistedEntity = notPersistedEntity;
        this.persistentEntity = persistentEntity;
    }

    @BeforeEach
    abstract void setUp();

//    @Test
//    void testCreateResource() {
//        when(mediaResourceDao.save(notPersistedEntity)).thenReturn(persistentEntity);
//
//        T createdResource = mediaResourceService.createResource(notPersistedEntity);
//
//        assertEquals(persistentEntity, createdResource);
//        verify(mediaResourceDao).save(notPersistedEntity);
//    }

    @Test
    void testDelete() {
        when(mediaResourceDao.existsById(any())).thenReturn(true);
        mediaResourceService.delete(persistentEntity.getId());

        assertTrue(mediaResourceDao.existsById(persistentEntity.getId()));
    }

    @Test
    void testFindById() {
        when(mediaResourceDao.findById(persistentEntity.getId())).thenReturn(Optional.of(persistentEntity));
    }
}

