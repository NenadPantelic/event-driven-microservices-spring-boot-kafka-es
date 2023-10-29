package com.np.twittertokafkaservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Slf4j
@Component
// all classes with @Component, @Configuration, @Service, @Repository and @Controller will be scanned and loaded as beans
// at runtime
public class TwitterKafkaStatusListener extends StatusAdapter {

    @Override
    public void onStatus(Status status) {
        log.info("Twitter status with text {}", status.getText());
    }
}
