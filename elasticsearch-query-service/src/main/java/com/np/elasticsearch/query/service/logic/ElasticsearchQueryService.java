package com.np.elasticsearch.query.service.logic;

import com.np.elasticsearch.query.service.model.ElasticsearchQueryServiceResponseModel;

import java.util.List;

public interface ElasticsearchQueryService {

    ElasticsearchQueryServiceResponseModel getDocumentById(String id);

    List<ElasticsearchQueryServiceResponseModel> getDocumentsByText(String text);

    List<ElasticsearchQueryServiceResponseModel> getAllDocuments();
}
