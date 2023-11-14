package com.np.kafka.admin.client;

import com.np.config.KafkaConfigData;
import com.np.config.RetryConfigData;
import com.np.kafka.admin.exception.KafkaClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaAdminClient {

    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;

    private final WebClient webClient;

    public KafkaAdminClient(KafkaConfigData kafkaConfigData,
                            RetryConfigData retryConfigData,
                            AdminClient adminClient,
                            RetryTemplate retryTemplate,
                            WebClient webClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.retryConfigData = retryConfigData;
        this.adminClient = adminClient;
        this.retryTemplate = retryTemplate;
        this.webClient = webClient;

        log.info("DEBUGCIC - {}", kafkaConfigData);
    }

    public void createTopics() {
        CreateTopicsResult createTopicsResult;
        try {
            createTopicsResult = retryTemplate.execute(this::doCreateTopics);
        } catch (Throwable throwable) {
            throw new KafkaClientException("Reached maxxxx number of retries when trying to create Kafka topics.");
        }

        checkIfTopicsCreated();
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        List<String> topicsToCreate = kafkaConfigData.getTopicNamesToCreate();
        log.info("Creating topics {}, attempt {}", topicsToCreate, retryContext.getRetryCount());

        List<NewTopic> topics = topicsToCreate.stream().map(topic -> new NewTopic(
                topic.trim(),
                kafkaConfigData.getNumOfPartitions(),
                kafkaConfigData.getReplicationFactor()
        )).collect(Collectors.toList());

        return adminClient.createTopics(topics);
    }

    private void checkIfTopicsCreated() {
        Collection<TopicListing> topics = getTopics();
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeInMs = retryConfigData.getSleepTimeMs();

        for (String topicToCreate : kafkaConfigData.getTopicNamesToCreate()) {
            while (!isTopicCreated(topics, topicToCreate)) {
                checkMaxRetry(retryCount++, maxRetry);
                sleep(sleepTimeInMs);
                sleepTimeInMs += multiplier;
                topics = getTopics();
            }
        }
    }

    public void checkSchemaRegistry() {
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeInMs = retryConfigData.getSleepTimeMs();

        while (!getSchemaRegistryStatus().is2xxSuccessful()) {
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTimeInMs);
            sleepTimeInMs += multiplier;
        }
    }

    private HttpStatus getSchemaRegistryStatus() {
        try {
            return webClient
                    .method(HttpMethod.GET)
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchange()
                    .map(ClientResponse::statusCode)
                    .block();
        } catch (Exception e) {
            log.warn("An error occurred while pinging the schema registry.", e);
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    private void sleep(Long sleepTimeInMs) {
        try {
            Thread.sleep(sleepTimeInMs);
        } catch (InterruptedException e) {
            throw new KafkaClientException("Error while sleeping.");
        }
    }

    private void checkMaxRetry(int retry, Integer maxRetry) {
        if (retry > maxRetry) {
            throw new KafkaClientException("Reached max number of retry for reading Kafka topic(s)");
        }
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicName) {
        if (topics == null) {
            return false;
        }

        return topics.stream().anyMatch(topic -> topic.name().equals(topicName));
    }

    private Collection<TopicListing> getTopics() {
        try {
            return retryTemplate.execute(this::doGetTopics);
        } catch (Throwable throwable) {
            throw new KafkaClientException("Reached max number of retries when reading Kafka topic(s)!");
        }
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        log.info(
                "Reading Kafka topic {}, attempt {}",
                kafkaConfigData.getTopicNamesToCreate(), retryContext.getRetryCount()
        );
        Collection<TopicListing> topics = adminClient.listTopics().listings().get();
        if (topics != null) {
            topics.forEach(topic -> log.info("Topic with name {}", topic.name()));
        }

        return topics;
    }
}
