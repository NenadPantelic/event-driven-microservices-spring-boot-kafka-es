package com.np.reactive.elasticsearch.query.client.service;

import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientRequest;
import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientResponse;
import reactor.core.publisher.Flux;

public interface ElasticsearchQueryWebClient {

    Flux<ElasticsearchQueryWebClientResponse> searchDocumentsByText(ElasticsearchQueryWebClientRequest request);
}
