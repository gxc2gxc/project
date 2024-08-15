package com.xuchen.project.delay;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.xuchen.project.delay.mapper")
@EnableFeignClients(basePackages = "com.xuchen.project.api.delay")
public class DelayApplication {
    public static void main(String[] args) {
        SpringApplication.run(DelayApplication.class, args);
    }
}