package com.np.reactive.elasticsearch.query.service.logic;

import com.np.elastic.model.index.IndexModel;
import com.np.elastic.model.index.impl.TwitterIndexModel;
import reactor.core.publisher.Flux;

public interface ReactiveElasticsearchQueryClient<T extends IndexModel> {

    Flux<TwitterIndexModel> getIndexModelsByText(String text);
}
