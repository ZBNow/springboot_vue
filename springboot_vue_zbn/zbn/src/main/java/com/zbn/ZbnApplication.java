package com.zbn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zbn.mapper")//扫描mapper接口层
public class ZbnApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZbnApplication.class, args);
    }

}
