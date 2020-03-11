package com.sx.mvc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shengx
 * @date 2020/3/9 10:12
 */
public class ModelAndView {
    private String viewName;
    private Map<String, Object> resultMap;
    private int status;

    public ModelAndView() {
        this.resultMap = new HashMap<>();
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void addObject(String name, Object obj) {
        resultMap.put(name, obj);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ModelAndView{" +
                "viewName='" + viewName + '\'' +
                ", resultMap=" + resultMap +
                ", status=" + status +
                '}';
    }
}
