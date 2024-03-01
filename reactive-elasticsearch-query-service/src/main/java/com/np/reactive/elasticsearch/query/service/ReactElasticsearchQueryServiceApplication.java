package com.np.reactive.elasticsearch.query.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.np")
@SpringBootApplication
public class ReactElasticsearchQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactElasticsearchQueryServiceApplication.class, args);
    }
}