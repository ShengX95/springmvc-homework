package com.sx.service;

import com.sx.pojo.Resume;

import java.util.List;

public interface ResumeService {
    public Resume findResumeById(Integer id);
    public List<Resume> findAllResume();
    public boolean modifyResume(Resume resume);
    public boolean removeResumeById(Integer id);
    public boolean addResume(Resume resume);
}
