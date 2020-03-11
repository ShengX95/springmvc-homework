package com.sx.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sx.pojo.Resume;
import com.sx.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author shengx
 * @date 2020/3/10 15:36
 */
@Controller
@RequestMapping("/resume")
public class ResumeController {
    @Autowired
    private ResumeService resumeService;

    @RequestMapping("/list")
    public ModelAndView list(ModelAndView modelAndView, HttpServletRequest req){
        List<Resume> resumes = resumeService.findAllResume();
        System.out.println(resumes);
        req.setAttribute("resumes", resumes);
        modelAndView.addObject("resumes", resumes);
        modelAndView.setViewName("list");
        return modelAndView;
    }
    @RequestMapping("/getResumeJson")
    @ResponseBody
    public Resume getResumeJson(Integer id){
        Resume resume = resumeService.findResumeById(id);
        return resume;
    }

    @RequestMapping("/delete")
    public String deleteResume(Integer id){
        System.out.println("delete:" + id);
        resumeService.removeResumeById(id);
        return "redirect:/resume/list";
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView updatePage(ModelAndView modelAndView, Integer id){
        Resume resume = resumeService.findResumeById(id);
        modelAndView.addObject("resume", resume);
        modelAndView.setViewName("update");
        return modelAndView;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String doUpdate(Resume resume){
        System.out.println(resume);
        resumeService.modifyResume(resume);
        return "redirect:/resume/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAdd(){
        return "add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String doAdd(Resume resume){
        resumeService.addResume(resume);
        return "redirect:/resume/list";
    }
}
