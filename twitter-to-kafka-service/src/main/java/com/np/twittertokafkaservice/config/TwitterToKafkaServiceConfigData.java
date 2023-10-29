package com.np.twittertokafkaservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "twitter-to-kafka-service")
public class TwitterToKafkaServiceConfigData {

    // NOTE: the name must match the naming in the application.yml file (camel-cased)
    private List<String> twitterKeywords;
    private boolean enableMockTweets;
    private int mockMinTweetLength;
    private int mockMaxTweetLength;
    private long mockSleepMs;
}
