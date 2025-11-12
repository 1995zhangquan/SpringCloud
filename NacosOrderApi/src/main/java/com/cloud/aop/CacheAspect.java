package com.cloud.aop;

import com.alibaba.fastjson.JSON;
import com.cloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class CacheAspect {

    @Resource
    private RedisUtil redisUtil;

    //aop切点，拦截指定注解修饰的方法
    @Pointcut("@annotation(com.cloud.aop.Cache)")
    public void cache() {
    }

    //缓存操作
    @Around("cache()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Signature signature = joinPoint.getSignature();
            //类名
            String className = joinPoint.getTarget().getClass().getSimpleName();
            //方法名
            String methodName = signature.getName();
            //参数处理
            Object[] args = joinPoint.getArgs();
            Class[] parameterTypes = new Class[args.length];
            String params = "";
            for (int i =0; i < args.length; i++) {
                if (null != args[i]) {
                    parameterTypes[i] = args[i].getClass();
                    params += JSON.toJSONString(args[i]);
                }
            }
            if (StringUtils.isNotEmpty(params)) {
                //加密防止出现key过长以及字符转移获取不到的情况
                params = DigestUtils.md5Hex(params);
            }

            //获取controller的方法
            Method method = signature.getDeclaringType().getMethod(methodName, parameterTypes);
            //获取cache注解
            Cache annotation = method.getAnnotation(Cache.class);
            long expire = annotation.expire();
            String name = annotation.name();
            //访问redis，没有则访问数据库
            String redisKey = name + "::" + className+ "::" + methodName + "::" + params;
            String redisValue = String.valueOf(redisUtil.get(redisKey));
            if (!"null".equals(redisValue) && StringUtils.isNotEmpty(redisValue)) {
                //不为空返回数据
                long expire1 = redisUtil.getExpire(redisKey);
                log.info("redis缓存时间：{}s", expire1);

                Class<?> returnType = method.getReturnType();
                Object result = JSON.parseObject(redisValue, returnType);
                log.info("数据从redis缓存中获取，key：{}", redisKey);
                return result;
            }
            Object proceed = joinPoint.proceed();
            redisUtil.set(redisKey, JSON.toJSONString(proceed), expire, TimeUnit.MINUTES);
            log.info("数据存入redis缓存，key：{}", redisKey);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            // 当缓存切面发生异常时，直接执行原方法而不是返回ApiResult
            return joinPoint.proceed();
        }
    }

}