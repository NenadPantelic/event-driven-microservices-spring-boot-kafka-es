package com.np.elasticsearch.query.service.logic.impl;

import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.elasticsearch.query.client.service.ElasticsearchQueryClient;
import com.np.elasticsearch.query.service.common.model.ElasticsearchQueryServiceResponseModel;
import com.np.elasticsearch.query.service.logic.ElasticsearchQueryService;
import com.np.elasticsearch.query.service.model.assembler.ElasticsearchQueryServiceResponseModelAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TwitterElasticsearchQueryService implements ElasticsearchQueryService {

    private final ElasticsearchQueryClient<TwitterIndexModel> elasticsearchQueryClient;
    private final ElasticsearchQueryServiceResponseModelAssembler assembler;

    public TwitterElasticsearchQueryService(ElasticsearchQueryClient<TwitterIndexModel> elasticsearchQueryClient,
                                            ElasticsearchQueryServiceResponseModelAssembler assembler) {
        this.elasticsearchQueryClient = elasticsearchQueryClient;
        this.assembler = assembler;
    }

    @Override
    public ElasticsearchQueryServiceResponseModel getDocumentById(String id) {
        log.info("Get document by id '{}'", id);
        return assembler.toModel(elasticsearchQueryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticsearchQueryServiceResponseModel> getDocumentsByText(String text) {
        log.info("Get documents by text '{}'", text);
        return assembler.toModels(elasticsearchQueryClient.getIndexModelByText(text));
    }

    @Override
    public List<ElasticsearchQueryServiceResponseModel> getAllDocuments() {
        log.info("Get all documents...");
        return assembler.toModels(elasticsearchQueryClient.getAllIndexModels());
    }
}
