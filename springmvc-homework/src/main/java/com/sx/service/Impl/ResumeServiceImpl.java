package com.sx.service.Impl;

import com.sun.org.apache.regexp.internal.RE;
import com.sx.dao.ResumeDao;
import com.sx.pojo.Resume;
import com.sx.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shengx
 * @date 2020/3/10 15:17
 */
@Service
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private ResumeDao resumeDao;
    @Override
    public Resume findResumeById(Integer id) {
        return resumeDao.findById(id).get();
    }

    @Override
    public List<Resume> findAllResume() {
        return resumeDao.findAll();
    }

    @Override
    public boolean modifyResume(Resume resume) {
        resumeDao.save(resume);
        return true;
    }

    @Override
    public boolean removeResumeById(Integer id) {
        resumeDao.deleteById(id);
        return true;
    }

    @Override
    public boolean addResume(Resume resume) {
        resumeDao.save(resume);
        return true;
    }
}
