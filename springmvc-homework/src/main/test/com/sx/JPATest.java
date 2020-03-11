package com.sx;

import com.sun.org.apache.regexp.internal.RE;
import com.sx.dao.ResumeDao;
import com.sx.pojo.Resume;
import com.sx.service.ResumeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * @author shengx
 * @date 2020/3/10 16:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:application*.xml"})
public class JPATest {
    @Autowired
    ResumeDao resumeDao;

    @Autowired
    ResumeService resumeService;
    @Test
    public void test(){
        System.out.println(resumeService.findResumeById(1));
        Resume resume = new Resume();
        resume.setId(7);
        resume.setName("5");
        resume.setAddress("555");
        resume.setPhone("4444");
        resumeService.removeResumeById(7);
    }
}
