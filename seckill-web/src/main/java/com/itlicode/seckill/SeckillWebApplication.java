package com.itlicode.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ClassName SeckillWebApplication
 * @Description 请描述类的业务用途
 * @Author liweijian
 * @Date 2020-12-29 10:41
 * @Version 1.0
 **/
@SpringBootApplication
@ComponentScan(basePackages= {"com.itlicode.*"})
@MapperScan("com.itlicode.mapper")

public class SeckillWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillWebApplication.class, args);
    }
}
