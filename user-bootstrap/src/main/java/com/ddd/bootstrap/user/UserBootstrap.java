package com.ddd.bootstrap.user;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
//@EnableFeignClients(basePackages = {"com.ddd"})
@ComponentScan(basePackages = {"com.ddd"})
@MapperScan({"com.ddd.infrastructure.*.mapper"})
@Slf4j
@EnableAsync
public class UserBootstrap {

  public static void main(String[] args) {
    SpringApplication.run(UserBootstrap.class, args);
    log.info("user service started successfully.");
  }
}