package com.sx.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView);
}
