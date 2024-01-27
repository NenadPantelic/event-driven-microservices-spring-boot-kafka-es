package com.np.elasticsearch.index.client.service;

import com.np.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticsearchIndexClient<T extends IndexModel> {

    List<String> save(List<T> documents);
}
