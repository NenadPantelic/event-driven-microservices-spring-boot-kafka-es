package com.np.twittertokafkaservice.runner.impl;

import com.np.twittertokafkaservice.config.TwitterToKafkaServiceConfigData;
import com.np.twittertokafkaservice.listener.TwitterKafkaStatusListener;
import com.np.twittertokafkaservice.runner.StreamRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PreDestroy;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "twitter-to-kafka-service.enable-mock-tweets",
        havingValue = "false",
        matchIfMissing = true)
public class TwitterKafkaStreamRunner implements StreamRunner {

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;
    private TwitterStream twitterStream;

    public TwitterKafkaStreamRunner(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData,
                                    TwitterKafkaStatusListener twitterKafkaStatusListener) {
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
    }

    @Override
    public void start() throws TwitterException {
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterKafkaStatusListener);
        addFilter();
    }

    @PreDestroy // will close the stream here; this method will be called before the bean is destroyed
    // @Singleton - the same instance is shared each time the object is injected
    // @Prototype - a new object is created each time the object is injected
    // @PreDestroy is not called  for the objects with prototype scope
    public void shutdown() {
        if (twitterStream != null) {
            log.info("Closing twitter stream.  ");
            twitterStream.shutdown();
        }
    }

    private void addFilter() {
        String[] keywords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        log.info("Started filtering twitter stream for keywords {}", keywords);
    }
}
