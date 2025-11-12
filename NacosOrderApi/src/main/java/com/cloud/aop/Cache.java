package com.cloud.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    //过期时间，默认5分钟，CacheAspect设置
    long expire() default 5;
    //缓存名称
    String name() default "";


}
