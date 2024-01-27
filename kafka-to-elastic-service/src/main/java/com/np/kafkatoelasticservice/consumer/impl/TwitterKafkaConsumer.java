package com.np.kafkatoelasticservice.consumer.impl;

import com.np.config.KafkaConfigData;
import com.np.config.KafkaConsumerConfigData;
import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.elasticsearch.index.client.service.ElasticsearchIndexClient;
import com.np.kafka.admin.client.KafkaAdminClient;
import com.np.kafka.avro.model.TwitterAvroModel;
import com.np.kafkatoelasticservice.consumer.KafkaConsumer;
import com.np.kafkatoelasticservice.transformer.AvroToElasticsearchModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    private final AvroToElasticsearchModelTransformer avroToElasticModelTransformer;
    //
    private final ElasticsearchIndexClient<TwitterIndexModel> elasticIndexClient;

    public TwitterKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                KafkaAdminClient kafkaAdminClient,
                                KafkaConfigData kafkaConfigData,
                                KafkaConsumerConfigData kafkaConsumerConfigData,
                                AvroToElasticsearchModelTransformer avroToElasticModelTransformer,
                                ElasticsearchIndexClient<TwitterIndexModel> elasticIndexClient) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.avroToElasticModelTransformer = avroToElasticModelTransformer;
        this.elasticIndexClient = elasticIndexClient;
    }

    @EventListener // marks a method as a listener for application events
    public void onAppStarted(ApplicationStartedEvent applicationStartedEvent) {
        kafkaAdminClient.checkIfTopicsCreated();
        log.info("Topics with names {} are ready for operations", kafkaConfigData.getTopicNamesToCreate());
        Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer(
                kafkaConsumerConfigData.getConsumerGroupId())
        ).start();

//        kafkaListenerEndpointRegistry.getListenerContainer("twitterTopicListener").start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
//    @KafkaListener(id = "twitterTopicListener", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<TwitterAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("[threadId={}] {} number of messages received with keys {}, partitions {} and offsets {}; sending them" +
                " to ElasticSearch.", Thread.currentThread().getId(), messages.size(), keys, partitions, offsets);

        List<TwitterIndexModel> twitterIndexModels = avroToElasticModelTransformer.getElasticsearchModels(messages);
        List<String> documentIds = elasticIndexClient.save(twitterIndexModels);
        log.info("Documents saved to ElasticSearch with ids {}", documentIds.toArray());
    }
}
