package com.sx.service.impl;

import com.sx.anno.Service;
import com.sx.service.MyService;

/**
 * @author shengx
 * @date 2020/3/8 20:12
 */
@Service
public class MyServiceImpl implements MyService {
    public String getUserById(String id, String name){
        System.out.println("id:" + id + " name:" + name);
        return name;
    }
}
