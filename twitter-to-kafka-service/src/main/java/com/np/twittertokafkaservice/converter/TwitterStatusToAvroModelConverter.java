package com.np.twittertokafkaservice.converter;

import com.np.kafka.avro.model.TwitterAvroModel;
import twitter4j.Status;

public class TwitterStatusToAvroModelConverter {

    public static TwitterAvroModel convertTwitterStatusToTwitterAvroModel(Status status) {
        return TwitterAvroModel
                .newBuilder()
                .setId(status.getId())
                .setUserId(status.getUser().getId())
                .setText(status.getText())
                .setCreatedAt(status.getCreatedAt().getTime())
                .build();
    }
}
