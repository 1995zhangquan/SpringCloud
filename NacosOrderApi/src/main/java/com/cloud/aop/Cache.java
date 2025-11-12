package com.cloud.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    //过期时间，默认60s
    long expire() default 60 * 1000;
    //缓存名称
    String name() default "";
}
