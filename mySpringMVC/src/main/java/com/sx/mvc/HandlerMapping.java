package com.sx.mvc;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shengx
 * @date 2020/3/8 21:14
 */
public class HandlerMapping {
    private Map<String, HandlerMethod> handlerMethodMap;
    private List<Interceptor> interceptors;
    public HandlerMapping() {
        this.handlerMethodMap = new ConcurrentHashMap<>();
        this.interceptors = new ArrayList<>();
    }

    public void addHandlerMethod(String url, HandlerMethod handlerMethod){
        handlerMethodMap.put(url, handlerMethod);
    }

    public void addInterceptor(Interceptor interceptor){
        interceptors.add(interceptor);
    }
    public HandlerExecutionChain getHandler(String uri){
        HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain();
        HandlerMethod handlerMethod = getHandlerMethod(uri);
        List<Interceptor> interceptors = getInterceptors(uri);
        handlerExecutionChain.setHandlerMethod(handlerMethod);
        handlerExecutionChain.setInterceptors(interceptors);
        return handlerExecutionChain;
    }

    private List<Interceptor> getInterceptors(String uri) {
        return interceptors;
    }

    private HandlerMethod getHandlerMethod(String uri) {
        HandlerMethod handlerMethod = handlerMethodMap.get(uri);
        if(handlerMethod == null) {
            System.out.println("no mapping for:" + uri);
        }
        return handlerMethod;
    }
    
}
