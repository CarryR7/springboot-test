package com.focus.springboottest.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class ApiLogAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(public * com.focus.springboottest.controller..*(..))")
    public void apiLog() {}

    @Around("apiLog()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取请求体参数（标注了@RequestBody的）
        Object requestBody = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> {
                    Annotation[][] paramAnnotations = method.getParameterAnnotations();
                    for (Annotation[] annotations : paramAnnotations) {
                        for (Annotation annotation : annotations) {
                            if (annotation.annotationType().equals(RequestBody.class)) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .findFirst().orElse(null);

        System.out.println("====== 请求日志 ======");
        System.out.println("URL: " + request.getRequestURL());
        System.out.println("Method: " + request.getMethod());
        System.out.println("Params: " + objectMapper.writeValueAsString(request.getParameterMap()));
        System.out.println("Body: " + (requestBody != null ? objectMapper.writeValueAsString(requestBody) : "null"));

        Object result = joinPoint.proceed(); // 执行目标方法

        System.out.println("====== 响应日志 ======");
        System.out.println("Response: " + objectMapper.writeValueAsString(result));

        return result;
    }
}

