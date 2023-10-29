package com.np.twittertokafkaservice;

import com.np.twittertokafkaservice.config.TwitterToKafkaServiceConfigData;
import com.np.twittertokafkaservice.runner.StreamRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.ApplicationListener;
//
//import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
// @Scope("request") - scope of the bean, more details:
// https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch04s04.html
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final StreamRunner streamRunner;

    // constructor injection does not need @Autowired annotation
    // this way you can create immutable fields (final)
    // also, this way Spring does not use reflection (slower, antipattern, not secure) for injection, it's the plain Java code
    public TwitterToKafkaServiceApplication(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData,
                                            StreamRunner streamRunner) {
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
        this.streamRunner = streamRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("App starts...");
        log.info("Twitter keywords: {}", twitterToKafkaServiceConfigData.getTwitterKeywords());

        streamRunner.start();
    }

//    @PostConstruct // executed after dependency injection to perform initialization
//    public void init() {
//        // this code will be called once; once the bean is created
//    }

    // another way is to use @EventListener annotation
}

//// another way to initialize an app
//@SpringBootApplication
//class TwitterToKafkaServiceApplication2 implements ApplicationListener {
//
//    public static void main(String[] args) {
//        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
//    }
//
//    @Override
//    public void onApplicationEvent(ApplicationEvent event) {
//        // will be called only once
//    }
//
////    @PostConstruct // executed after dependency injection to perform initialization
////    public void init() {
////        // this code will be called once; once the bean is created
////    }
//
//
//}