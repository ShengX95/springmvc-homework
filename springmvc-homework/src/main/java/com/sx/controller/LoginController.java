package com.sx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shengx
 * @date 2020/3/10 19:43
 */
@Controller
@RequestMapping("/")
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLogin(){
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(ModelAndView modelAndView, HttpServletRequest request, String username, String password){
        System.out.println(username + " " + password);
        if("admin".equals(username) && "admin".equals(password)){
            request.getSession().setAttribute("username", username);
            return "forward:resume/list";
        }else{
            request.setAttribute("error", "username or password error");
            return "forward:login";
        }
    }
    @RequestMapping("/index")
    public String index(){
        Integer i = 1;
        i.byteValue();
        return "index";
    }
}
