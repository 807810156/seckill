package com.itlicode.service.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName ProcessAspect
 * @Description 服务日志切面实现类
 * @Author liweijian
 * @Date 2020-09-06 14:34
 * @Version 1.0
 **/
@Component
@Aspect
public class ProcessAspect {

    /**
     * 日志服务
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());


    private static final Integer CODE = 100;


    /**
     * 构造器
     */
    public ProcessAspect() {
    }

    /**
     * 切点1,保存工单服务日志信息
     */
    @Pointcut("@annotation(com.itlicode.service.impl.ProcessAnnotation)")
    public void saveProcessCut() {

    }

    /**
     * 后置操作
     * @param joinPoint 封装了代理方法信息的对象
     * @param ginseng 方法出参
     * @return
     * @throws Throwable
     */
    @AfterReturning(pointcut = "saveProcessCut()", returning = "ginseng")
    public void saveProcessAfter(JoinPoint joinPoint, Object ginseng) throws Exception {
        logger.error("调试");
        throw new Exception("测试");
    }
}
