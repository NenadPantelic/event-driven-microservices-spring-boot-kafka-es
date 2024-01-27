package com.np.elasticsearch.query.client.service.impl;

import com.np.config.ElasticSearchQueryConfigData;
import com.np.config.ElasticsearchConfigData;
import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.elasticsearch.query.client.exception.ElasticsearchQueryClientException;
import com.np.elasticsearch.query.client.service.ElasticsearchQueryClient;
import com.np.elasticsearch.query.client.util.ElasticSearchQueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TwitterElasticsearchQueryClient implements ElasticsearchQueryClient<TwitterIndexModel> {


    private final ElasticsearchConfigData elasticsearchConfigData;
    private final ElasticSearchQueryConfigData elasticSearchQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticSearchQueryUtil<TwitterIndexModel> elasticSearchQueryUtil;

    public TwitterElasticsearchQueryClient(ElasticsearchConfigData elasticsearchConfigData,
                                           ElasticSearchQueryConfigData elasticSearchQueryConfigData,
                                           ElasticsearchOperations elasticsearchOperations,
                                           ElasticSearchQueryUtil<TwitterIndexModel> elasticSearchQueryUtil) {
        this.elasticsearchConfigData = elasticsearchConfigData;
        this.elasticSearchQueryConfigData = elasticSearchQueryConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticSearchQueryUtil = elasticSearchQueryUtil;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Query query = elasticSearchQueryUtil.getSearchQueryById(id);
        SearchHit<TwitterIndexModel> searchResult = elasticsearchOperations.searchOne(
                query,
                TwitterIndexModel.class,
                IndexCoordinates.of(elasticsearchConfigData.getIndexName())
        );

        if (searchResult == null) {
            String errorMessage = String.format("No document found at ES with id %s", id);
            log.warn(errorMessage);
            throw new ElasticsearchQueryClientException(errorMessage);
        }

        log.info("Document with id {} retrieved successfully", id);
        return searchResult.getContent();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        Query query = elasticSearchQueryUtil.getSearchQueryByFieldText(
                elasticSearchQueryConfigData.getTextField(), text
        );
        return search(query, "{} of documents with text {} retrieved successfully.", text);
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Query query = elasticSearchQueryUtil.getSearchQueryForAll();
        SearchHits<TwitterIndexModel> searchResult = elasticsearchOperations.search(
                query,
                TwitterIndexModel.class,
                IndexCoordinates.of(elasticsearchConfigData.getIndexName())
        );

        log.info("{} number of documents retrieved successfully.", searchResult.getTotalHits());
        return search(query, "{} of documents retrieved successfully.");
    }

    private List<TwitterIndexModel> search(Query query, String logMessage, Object... logParams) {
        SearchHits<TwitterIndexModel> searchResult = elasticsearchOperations.search(
                query,
                TwitterIndexModel.class,
                IndexCoordinates.of(elasticsearchConfigData.getIndexName())
        );

        log.info(logMessage, searchResult.getTotalHits(), logParams);

        return searchResult.get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
