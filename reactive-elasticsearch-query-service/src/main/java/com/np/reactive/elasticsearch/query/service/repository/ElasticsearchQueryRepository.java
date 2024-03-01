package com.np.reactive.elasticsearch.query.service.repository;

import com.np.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface ElasticsearchQueryRepository extends ReactiveCrudRepository<TwitterIndexModel, String> {

    // Mono emits 0 to 1 element
    // Flux emits 0 to n elements
    Flux<TwitterIndexModel> findByText(String text);
}
