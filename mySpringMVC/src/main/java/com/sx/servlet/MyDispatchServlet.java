package com.sx.servlet;

import com.sx.anno.*;
import com.sx.mvc.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author shengx
 * @date 2020/3/8 17:45
 */
public class MyDispatchServlet extends HttpServlet {
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private List<Class> scanClass = new ArrayList<>();
    private HandlerMapping mapping = new HandlerMapping();
    Properties properties = new Properties();

    @Override
    public void init(ServletConfig config) throws ServletException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(config.getInitParameter("contextConfigLocation"));
        // 初始化IoC容器
        initIoC(in);
        // 初始化handlerMapping
        initHandlerMapping();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    protected void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandlerExecutionChain handler = mapping.getHandler(req.getRequestURI());
        ModelAndView modelAndView = null;
        try {
            if(handler.getHandlerMethod() == null){
                req.getRequestDispatcher("/404.jsp").forward(req, resp);
                return;
            }
            if(!handler.applyPreHandler(req, resp)){
                return;
            }
            modelAndView = handler.handle(req, resp);
            handler.applyPostHandler(req, resp, modelAndView);
            render(req, resp, modelAndView);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 视图渲染
    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) {
        System.out.println("视图渲染:" + modelAndView.toString());
        try {
            Map<String, Object> resultMap = modelAndView.getResultMap();
            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                req.setAttribute(entry.getKey(), entry.getValue());
            }
            req.getRequestDispatcher(modelAndView.getViewName()).forward(req, resp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
    // 初始化IoC容器
    private void initIoC(InputStream in) {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);
            Element root = document.getRootElement();
            List<Element> list = root.selectNodes("//component-scan");
            if(list.size()>0){
                String basePackage = list.get(0).attributeValue("basePage");
                // 扫包
                doScan(basePackage);
            }
            // 实例化bean
            doInit();
            // 依赖注入
            doAutowired();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    // autowired依赖注入
    private void doAutowired() throws IllegalAccessException {
        for (Map.Entry<String, Object> entry : singletonObjects.entrySet()) {
            String name = entry.getKey();
            Object instance = entry.getValue();
            Class clazz = instance.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    Object diObj = singletonObjects.get(field.getType().getName());
                    if (diObj != null) {
                        field.set(instance, diObj);
                    }
                }
            }
        }
    }

    // 根据扫描出的class文件，初始化bean
    private void doInit() throws IllegalAccessException, InstantiationException {
        if (scanClass.size() > 0) {
            for (Class aClass : scanClass) {
                if (aClass.isAnnotationPresent(Controller.class)) {
                    Object instance = aClass.newInstance();
                    String instanceName = lowerFirst(aClass.getSimpleName());
                    singletonObjects.put(instanceName, instance);
                } else if (aClass.isAnnotationPresent(Service.class)) {
                    Object instance = aClass.newInstance();
                    String instanceName = lowerFirst(aClass.getSimpleName());
                    singletonObjects.put(instanceName, instance);
                    Class[] interfaces = aClass.getInterfaces();
                    for (Class anInterface : interfaces) {
                        singletonObjects.put(anInterface.getName(), instance);
                    }
                } else if(aClass.isAnnotationPresent(Component.class)){
                    Object instance = aClass.newInstance();
                    // 拦截器实例生成
                    if(instance instanceof Interceptor){
                        mapping.addInterceptor((Interceptor) instance);
                    }
                }
            }
        }
    }

    // 扫包初始化IOC容器
    private void doScan(String basePackage) throws ClassNotFoundException {
        String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + basePackage.replaceAll("\\.", "/");
        File pack = new File(basePath);
        File[] files = pack.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                doScan(basePackage + "." + file.getName().replaceAll(".class", ""));
            }
            // 如果是class文件，加载class文件
            else if (file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replaceAll(".class", "");
                Class clazz = Class.forName(className);
                scanClass.add(clazz);
            }
        }
    }

    // 初始化handlerMapping
    private void initHandlerMapping() {
        for (Map.Entry<String, Object> entry : singletonObjects.entrySet()) {
            String name = entry.getKey();
            Object instance = entry.getValue();
            Class clazz = instance.getClass();
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                String baseUrl = requestMapping.value();
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if(method.isAnnotationPresent(RequestMapping.class)){
                        HandlerMethod handlerMethod = new HandlerMethod();
                        String methodUrl = method.getAnnotation(RequestMapping.class).value();
                        String fullUrl = baseUrl + methodUrl;
                        Pattern pattern = Pattern.compile(fullUrl);
                        // 请求参数映射
                        Parameter[] parameters = method.getParameters();
                        Map<String, Integer> parameterMap = new HashMap<>();
                        for (int i = 0; i < parameters.length; i++) {
                            Parameter parameter = parameters[i];
                            Class parameterClass = parameter.getType();
                            if(parameterClass == HttpServletRequest.class || parameterClass == HttpServletResponse.class || parameterClass == ModelAndView.class){
                                String parameterName = parameterClass.getName();
                                parameterMap.put(parameterName, i);
                            }else{
                                String parameterName = parameter.getName();
                                parameterMap.put(parameterName, i);
                            }
                        }
                        handlerMethod.setMethod(method);
                        handlerMethod.setController(instance);
                        handlerMethod.setUrlPattern(pattern);
                        handlerMethod.setParameterMap(parameterMap);
                        mapping.addHandlerMethod(fullUrl, handlerMethod);
                    }
                }
            }
        }
    }

    private String lowerFirst(String str) {
        str = str.substring(0, 1).toLowerCase() + str.substring(1);
        return str;
    }


}
