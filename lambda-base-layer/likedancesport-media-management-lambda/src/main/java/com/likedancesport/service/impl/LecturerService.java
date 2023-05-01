package com.likedancesport.service.impl;

import com.likedancesport.common.dao.ILecturerDao;
import com.likedancesport.common.model.domain.learning.Lecturer;
import com.likedancesport.request.LecturerUpdateRequest;
import com.likedancesport.service.ILecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturerService implements ILecturerService {
    private final ILecturerDao lecturerDao;

    @Autowired
    public LecturerService(ILecturerDao lecturerDao) {
        this.lecturerDao = lecturerDao;
    }

    @Override
    public List<Lecturer> findAll() {
        return lecturerDao.findAll();
    }

    @Override
    public Page<Lecturer> findAllPaginated(Pageable pageable) {
        return lecturerDao.findAll(pageable);
    }

    @Override
    public Lecturer getLecturer(Long lecturerId) {
        return lecturerDao.findById(lecturerId).orElseThrow();
    }

    @Override
    public Lecturer createLecturer(Lecturer lecturer) {
        return lecturerDao.save(lecturer);
    }

    @Override
    public Lecturer updateLecturer(Long lecturerId, LecturerUpdateRequest updateRequest) {
        Lecturer lecturer = lecturerDao.findById(lecturerId).orElseThrow();
        lecturer.setName(updateRequest.getName());
        lecturer.setDescription(updateRequest.getDescription());
        return null;
    }

    @Override
    public void delete(Long lecturerId) {
        lecturerDao.deleteById(lecturerId);
    }
}
