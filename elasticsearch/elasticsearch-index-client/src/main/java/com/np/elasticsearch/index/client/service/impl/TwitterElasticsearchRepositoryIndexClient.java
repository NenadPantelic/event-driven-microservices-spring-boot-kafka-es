package com.np.elasticsearch.index.client.service.impl;

import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.elasticsearch.index.client.repository.TwitterElasticsearchIndexRepository;
import com.np.elasticsearch.index.client.service.ElasticsearchIndexClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@Primary // give higher precedence to a bean
@ConditionalOnProperty(name = "elasticsearch-config.use-repository", havingValue = "true", matchIfMissing = true)
@Slf4j
@Service
public class TwitterElasticsearchRepositoryIndexClient implements ElasticsearchIndexClient<TwitterIndexModel> {

    private final TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository;

    public TwitterElasticsearchRepositoryIndexClient(TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository) {
        this.twitterElasticsearchIndexRepository = twitterElasticsearchIndexRepository;
    }

    // less code, more fluent, but less flexibility than using ES operations
    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> repositoryResponse = (List<TwitterIndexModel>)
                twitterElasticsearchIndexRepository.saveAll(documents);
        List<String> documentIds = repositoryResponse.stream()
                .map(TwitterIndexModel::getId)
                .collect(Collectors.toList());

        log.info(
                "Documents indexed successfully with type {} and ids {}",
                TwitterIndexModel.class.getName(), documentIds
        );
        return documentIds;
    }
}
