package com.np.twittertokafkaservice.listener;

import com.np.config.KafkaConfigData;
import com.np.kafka.avro.model.TwitterAvroModel;
import com.np.kafka.producer.service.KafkaProducer;
import com.np.twittertokafkaservice.converter.TwitterStatusToAvroModelConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Slf4j
@Component
// all classes with @Component, @Configuration, @Service, @Repository and @Controller will be scanned and loaded as beans
// at runtime
public class TwitterKafkaStatusListener extends StatusAdapter {

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;

    public TwitterKafkaStatusListener(KafkaConfigData kafkaConfigData,
                                      KafkaProducer<Long, TwitterAvroModel> kafkaProducer) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void onStatus(Status status) {
        log.info("Received status text {}. Sending to Kafka topic {}", status.getText(), kafkaConfigData.getTopicName());
        TwitterAvroModel twitterAvroModel = TwitterStatusToAvroModelConverter.convertTwitterStatusToTwitterAvroModel(
                status
        );
        // partition key is the user id
        kafkaProducer.send(kafkaConfigData.getTopicName(), twitterAvroModel.getUserId(), twitterAvroModel);
    }
}
