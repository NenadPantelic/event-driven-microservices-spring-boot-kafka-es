package com.np.elasticsearch.query.client.service.impl;

import com.np.common.util.CollectionsUtil;
import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.elasticsearch.query.client.exception.ElasticsearchQueryClientException;
import com.np.elasticsearch.query.client.repository.TwitterElasticsearchQueryRepository;
import com.np.elasticsearch.query.client.service.ElasticsearchQueryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Primary // priority over the other implementations
@Service
public class TwitterElasticsearchRepositoryQueryClient implements ElasticsearchQueryClient<TwitterIndexModel> {

    private final TwitterElasticsearchQueryRepository twitterElasticsearchQueryRepository;

    public TwitterElasticsearchRepositoryQueryClient(TwitterElasticsearchQueryRepository twitterElasticsearchQueryRepository) {
        this.twitterElasticsearchQueryRepository = twitterElasticsearchQueryRepository;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Optional<TwitterIndexModel> searchResult = twitterElasticsearchQueryRepository.findById(id);
        log.info(
                "Document with id {} retrieved successfully.",
                searchResult.orElseThrow(() -> new ElasticsearchQueryClientException(
                                String.format("No document found in ES with id %s.", id)
                        )
                )
        );
        return searchResult.get();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        List<TwitterIndexModel> searchResult = twitterElasticsearchQueryRepository.findByText(text);
        log.info("{} of documents with text {} retrieved successfully", searchResult.size(), text);
        return searchResult;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        List<TwitterIndexModel> searchResult = CollectionsUtil.getInstance()
                .getListFromIterable(twitterElasticsearchQueryRepository.findAll());
        log.info("{} number of documents retrieved successfully", searchResult.size());
        return searchResult;
    }
}
