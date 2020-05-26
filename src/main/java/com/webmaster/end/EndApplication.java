package com.webmaster.end;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.webmaster.end.IMapper")
@EnableScheduling
public class EndApplication {

    public static void main(String[] args) {
        SpringApplication.run(EndApplication.class, args);
    }

}
