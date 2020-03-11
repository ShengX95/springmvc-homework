package com.sx.mvc;

import com.sun.org.apache.xpath.internal.operations.Mod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author shengx
 * @date 2020/3/9 10:03
 */
public class HandlerExecutionChain {
    private HandlerMethod handlerMethod;
    private List<Interceptor> interceptors;

    public boolean applyPreHandler(HttpServletRequest request, HttpServletResponse response){
        if(interceptors != null && interceptors.size()>0){
            for (Interceptor interceptor : interceptors) {
                boolean flag = interceptor.preHandle(request, response, handlerMethod);
                if(!flag){
                    return false;
                }
            }
        }
        return true;
    }

    public void applyPostHandler(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView){
        if(interceptors != null && interceptors.size()>0){
            for (Interceptor interceptor : interceptors) {
                interceptor.postHandle(request, response, handlerMethod, modelAndView);
            }
        }
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        // Controller处理
        Map<String, String[]> parameterMap = request.getParameterMap();
        ModelAndView modelAndView = new ModelAndView();
        Object[] objects = null;
        Object controller = null;
        if(handlerMethod != null){
            Map<String, Integer> handlerParameterMap = handlerMethod.getParameterMap();
            objects = new Object[handlerParameterMap.size()];

            for (Map.Entry<String, Integer> entry : handlerParameterMap.entrySet()) {
                String name = entry.getKey();
                Integer index = entry.getValue();
                if(name.equals(HttpServletRequest.class.getName())){
                    objects[index] = request;
                } else if(name.equals(HttpServletResponse.class.getName())){
                    objects[index] = response;
                } else if(name.equals(ModelAndView.class.getName())){
                    objects[index] = modelAndView;
                } else {
                    String[] values = parameterMap.get(name);
                    if(values!=null && values.length>0){
                        String value = values[0];
                        objects[index] = value;
                    }
                }
            }
            Method method = handlerMethod.getMethod();
            controller = handlerMethod.getController();
            Object result = method.invoke(controller, objects);
            // 结果返回modelAndView
            if(result instanceof String){
                modelAndView.setViewName((String) result);
            }
        }
        return modelAndView;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }
}
