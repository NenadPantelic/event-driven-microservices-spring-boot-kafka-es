package com.np.twittertokafkaservice;

import com.np.twittertokafkaservice.init.StreamInitializer;
import com.np.twittertokafkaservice.runner.StreamRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@Slf4j
@ComponentScan(basePackages = "com.np")
@SpringBootApplication
// @Scope("request") - scope of the bean, more details:
// https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch04s04.html
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final StreamRunner streamRunner;
    private final StreamInitializer streamInitializer;

    // constructor injection does not need @Autowired annotation
    // this way you can create immutable fields (final)
    // also, this way Spring does not use reflection (slower, antipattern, not secure) for injection, it's the plain Java code
    public TwitterToKafkaServiceApplication(StreamRunner streamRunner, StreamInitializer streamInitializer) {
        this.streamRunner = streamRunner;
        this.streamInitializer = streamInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("App starts...");
        streamInitializer.init();
        streamRunner.start();
    }

}

/*
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.ApplicationListener;
//
//import javax.annotation.PostConstruct;
// another way to initialize an app
@SpringBootApplication
class TwitterToKafkaServiceApplication2 implements ApplicationListener {

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    // 1.
    @PostConstruct // executed after dependency injection to perform initialization
    public void init() {
        // this code will be called once; once the bean is created
    }

    // another way is to use @EventListener annotation
    // 2.
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        // will be called only once
    }
}*/
