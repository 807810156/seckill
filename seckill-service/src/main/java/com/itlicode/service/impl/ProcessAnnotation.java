package com.itlicode.service.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务日志切面注解
 * content:日志内容
 * title：日志标题
 * state： 工单状态
 * field：接口入参对象，需要获取那些字段
 * orderNoPlace：接口方法入参，工单号所在的位置
 * targetType：接口方法入参，工单号是什么类型
 * @author ext.liweijian
 * @className OrderProcessAnnotation
 * @date 2020-08-27 21:09:00
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProcessAnnotation {

    /**
     * 内容
     */
    String content();

    /**
     * 主题
     */
    String title();

    /**
     * 获取接口入参对象中的那些属性,工单号默认第一位,创建人第二位
     */
    String[] field() default {"orderNo", "createBy"};

    /**
     * 接口入参获取工单号对象所在的位置,必填
     */
    int orderNoPlace();

    /**
     * 进度类型: 0厂家回传进度; 1:上传售后ace进度
     */
    int type();

}
