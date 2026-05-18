package com.example.common.aspect;

import com.example.common.TraceContext;
import com.example.common.annotation.WebLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 【简历加分】AOP 切面，对标注 @WebLog 的方法进行入参、出参、耗时日志记录。
 */
@Aspect
@Component
public class WebLogAspect {
    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("@annotation(com.example.common.annotation.WebLog)")
    public void webLogPointcut() {}

    @Around("webLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        WebLog webLog = method.getAnnotation(WebLog.class);
        String description = webLog.value();

        // 构建日志信息
        String traceId = TraceContext.getTraceId();
        String url = request != null ? request.getRequestURI() : "";
        String httpMethod = request != null ? request.getMethod() : "";
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();

        log.info("【接口日志】TraceId:{} | 请求开始 | {} | {}.{} | URL:{} {} | 入参:{}",
                traceId, description, className, methodName, httpMethod, url,
                args != null ? objectMapper.writeValueAsString(args) : "");

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("【接口日志】TraceId:{} | 请求异常 | {}.{} | 异常:{}",
                    traceId, className, methodName, throwable.getMessage());
            throw throwable;
        }

        long timeCost = System.currentTimeMillis() - startTime;
        log.info("【接口日志】TraceId:{} | 请求结束 | {}.{} | 耗时:{}ms | 出参:{}",
                traceId, className, methodName, timeCost,
                result != null ? objectMapper.writeValueAsString(result) : "");

        return result;
    }
}