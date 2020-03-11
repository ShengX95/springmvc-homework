package com.sx.controller;

import com.sx.anno.Autowired;
import com.sx.anno.Controller;
import com.sx.anno.RequestMapping;
import com.sx.anno.Security;
import com.sx.mvc.ModelAndView;
import com.sx.service.MyService;

/**
 * @author shengx
 * @date 2020/3/8 19:33
 */
@Controller
@RequestMapping("/demo")
@Security({"shengx", "shengx2"})
public class MyController {
    @Autowired
    private MyService myService;
    @RequestMapping("/test")
    public ModelAndView test(ModelAndView modelAndView, String id, String username){
        modelAndView.addObject("id", id + "11");
        modelAndView.addObject("name", username + "11");
        myService.getUserById(id, username);
        modelAndView.setViewName("/index.jsp");
        return modelAndView;
    }

    @RequestMapping("/sec")
    @Security("shengx")
    public ModelAndView sec(ModelAndView modelAndView, String username){
        modelAndView.addObject("username", username);
        modelAndView.setViewName("/index.jsp");
        return modelAndView;
    }
}
