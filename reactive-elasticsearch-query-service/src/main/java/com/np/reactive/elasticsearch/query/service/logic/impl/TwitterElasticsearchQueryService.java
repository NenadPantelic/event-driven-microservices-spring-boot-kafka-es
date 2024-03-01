package com.np.reactive.elasticsearch.query.service.logic.impl;

import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.elasticsearch.query.service.common.model.ElasticsearchQueryServiceResponseModel;
import com.np.elasticsearch.query.service.common.transformer.ESDocumentToResponseModelTransformer;
import com.np.reactive.elasticsearch.query.service.logic.ElasticsearchQueryService;
import com.np.reactive.elasticsearch.query.service.logic.ReactiveElasticsearchQueryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class TwitterElasticsearchQueryService implements ElasticsearchQueryService {

    private final ReactiveElasticsearchQueryClient<TwitterIndexModel> reactiveElasticsearchQueryClient;

    public TwitterElasticsearchQueryService(ReactiveElasticsearchQueryClient<TwitterIndexModel> reactiveElasticsearchQueryClient) {
        this.reactiveElasticsearchQueryClient = reactiveElasticsearchQueryClient;
    }

    @Override
    public Flux<ElasticsearchQueryServiceResponseModel> getDocumentsByText(String text) {
        log.info("Querying reactive elasticsearch for text {}", text);
        return reactiveElasticsearchQueryClient
                .getIndexModelsByText(text)
                .map(ESDocumentToResponseModelTransformer::transformToResponseModel);
    }
}
