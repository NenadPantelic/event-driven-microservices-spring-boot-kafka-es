package com.np.elasticsearch.query.webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.np")
public class ElasticsearchQueryWebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchQueryWebClientApplication.class, args);
    }
}