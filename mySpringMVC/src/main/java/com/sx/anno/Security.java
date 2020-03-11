package com.sx.anno;

import java.lang.annotation.*;

/**
 * 注解在类上，则该类中所有方法默认权限为类定义用户集合
 * 如类和方法同事定义权限，则方法权限覆盖类权限
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Security {
    String[] value() default "";
}
