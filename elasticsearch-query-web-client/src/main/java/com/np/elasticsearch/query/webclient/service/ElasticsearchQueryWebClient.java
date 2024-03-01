package com.np.elasticsearch.query.webclient.service;

import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientRequest;
import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientResponse;
import java.util.List;

public interface ElasticsearchQueryWebClient {

    List<ElasticsearchQueryWebClientResponse> searchByText(ElasticsearchQueryWebClientRequest request);
}
