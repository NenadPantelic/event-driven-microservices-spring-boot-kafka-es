package com.np.kafkatoelasticservice.transformer;

import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvroToElasticsearchModelTransformer {

    public List<TwitterIndexModel> getElasticsearchModels(List<TwitterAvroModel> avroModels) {
        return avroModels.stream()
                .map(avroModel -> TwitterIndexModel.builder()
                        .userId(avroModel.getUserId())
                        .id(String.valueOf(avroModel.getId()))
                        .text(avroModel.getText())
                        .createdAt(
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(avroModel.getCreatedAt()), ZoneId.systemDefault())
                        )
                        .build())
                .collect(Collectors.toList());
    }
}
