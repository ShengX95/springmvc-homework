package com.sx.inter;

import com.sx.anno.Component;
import com.sx.anno.Security;
import com.sx.mvc.HandlerMethod;
import com.sx.mvc.Interceptor;
import com.sx.mvc.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author shengx
 * @date 2020/3/9 21:42
 */
@Component
public class SecurityInterceptor implements Interceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Security security = null;
            if (method.isAnnotationPresent(Security.class)) {
                security = method.getAnnotation(Security.class);
                String[] values = security.value();
                String username = request.getParameter("username");
                if (username == null) {
                    request.getRequestDispatcher("/security.jsp").forward(request, response);
                    return false;
                }else{
                    for (String value : values) {
                        if (value.equals(username)){
                            return true;
                        }
                    }
                }
                request.getRequestDispatcher("/security.jsp").forward(request, response);
                return false;
            } else if (method.getDeclaringClass().isAnnotationPresent(Security.class)) {
                security = method.getDeclaringClass().getAnnotation(Security.class);
                String[] values = security.value();
                String username = request.getParameter("username");
                if (username == null) {
                    request.getRequestDispatcher("/security.jsp").forward(request, response);
                    return false;
                } else {
                    for (String value : values) {
                        if (value.equals(username)){
                            return true;
                        }
                    }
                }
                request.getRequestDispatcher("/security.jsp").forward(request, response);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }
}
