package com.np.reactive.elasticsearch.query.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.np")
public class ReactiveElasticsearchQueryWebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveElasticsearchQueryWebClientApplication.class, args);
    }
}
