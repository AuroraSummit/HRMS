package com.hrms.config;

import com.hrms.common.constant.ApiConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.hrms.module..controller.*.*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String module = "";
        String operation = point.getSignature().getName();
        String requestUrl = request != null ? request.getRequestURI() : "";
        String requestMethod = request != null ? request.getMethod() : "";
        String requestParam = Arrays.toString(point.getArgs());
        String ip = request != null ? getIpAddress(request) : "";

        // Extract module name from package
        String className = point.getTarget().getClass().getName();
        if (className.contains("module.")) {
            String pkg = className.substring(className.indexOf("module.") + 7);
            if (pkg.contains(".")) {
                module = pkg.substring(0, pkg.indexOf("."));
            }
        }

        Object result = point.proceed();

        long duration = System.currentTimeMillis() - beginTime;

        log.info("【操作日志】模块: {}, 操作: {}, URL: {}, 方法: {}, 参数: {}, IP: {}, 耗时: {}ms, 结果: {}",
                module, operation, requestUrl, requestMethod, requestParam, ip, duration, result);

        return result;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
