package com.np.reactive.elasticsearch.query.service.logic.impl;

import com.np.config.ElasticsearchQueryServiceConfigData;
import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.reactive.elasticsearch.query.service.logic.ReactiveElasticsearchQueryClient;
import com.np.reactive.elasticsearch.query.service.repository.ElasticsearchQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Service
public class TwitterReactiveElasticsearchQueryClient implements ReactiveElasticsearchQueryClient<TwitterIndexModel> {

    private final ElasticsearchQueryRepository elasticsearchQueryRepository;
    private final ElasticsearchQueryServiceConfigData elasticsearchQueryServiceConfigData;

    public TwitterReactiveElasticsearchQueryClient(ElasticsearchQueryRepository elasticsearchQueryRepository,
                                                   ElasticsearchQueryServiceConfigData elasticsearchQueryServiceConfigData) {
        this.elasticsearchQueryRepository = elasticsearchQueryRepository;
        this.elasticsearchQueryServiceConfigData = elasticsearchQueryServiceConfigData;
    }

    @Override
    public Flux<TwitterIndexModel> getIndexModelsByText(String text) {
        log.info("Getting data from ES for text {}", text);
        return elasticsearchQueryRepository.findByText(text)
                .delayElements(Duration.ofMillis(elasticsearchQueryServiceConfigData.getBackpressureDelayMs()));
    }
}
