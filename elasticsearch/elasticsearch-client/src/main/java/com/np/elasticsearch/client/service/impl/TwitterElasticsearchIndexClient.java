package com.np.elasticsearch.client.service.impl;

import com.np.config.ElasticsearchConfigData;
import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.elasticsearch.client.service.ElasticsearchIndexClient;
import com.np.elasticsearch.client.util.ElasticSearchIndexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@ConditionalOnProperty(name = "elastic-config.use-repository", havingValue = "false")
@Slf4j
@Service
public class TwitterElasticsearchIndexClient implements ElasticsearchIndexClient<TwitterIndexModel> {


    private final ElasticsearchConfigData elasticsearchConfigData;
    // Index & Query against elasticsearch
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticSearchIndexUtil<TwitterIndexModel> elasticSearchIndexUtil;

    public TwitterElasticsearchIndexClient(ElasticsearchConfigData elasticsearchConfigData,
                                           ElasticsearchOperations elasticsearchOperations,
                                           ElasticSearchIndexUtil<TwitterIndexModel> elasticSearchIndexUtil) {
        this.elasticsearchConfigData = elasticsearchConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticSearchIndexUtil = elasticSearchIndexUtil;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<IndexQuery> indexQueries = elasticSearchIndexUtil.getIndexQueries(documents);
        List<String> documentIds = elasticsearchOperations.bulkIndex(
                indexQueries,
                IndexCoordinates.of(elasticsearchConfigData.getIndexName())
        );

        log.info(
                "Documents indexed successfully with type: {} and ids: {}",
                TwitterIndexModel.class.getName(), documentIds
        );
        return documentIds;
    }
}
