package com.np;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.np")
public class ElasticsearchQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchQueryServiceApplication.class, args);
    }
}

