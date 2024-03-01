package com.np.reactive.elasticsearch.query.service.logic;

import com.np.elasticsearch.query.service.common.model.ElasticsearchQueryServiceResponseModel;
import reactor.core.publisher.Flux;

public interface ElasticsearchQueryService {

    Flux<ElasticsearchQueryServiceResponseModel> getDocumentsByText(String text);
}
