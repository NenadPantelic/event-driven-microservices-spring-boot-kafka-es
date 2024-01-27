package com.np.elasticsearch.query.client.service;

import com.np.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticsearchQueryClient<T extends IndexModel> {

    T getIndexModelById(String id);

    List<T> getIndexModelByText(String text);

    List<T> getAllIndexModels();
}
