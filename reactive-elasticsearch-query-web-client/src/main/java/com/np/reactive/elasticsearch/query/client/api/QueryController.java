package com.np.reactive.elasticsearch.query.client.api;

import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientRequest;
import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientResponse;
import com.np.reactive.elasticsearch.query.client.service.ElasticsearchQueryWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@Slf4j
@RestController
public class QueryController {

    private final ElasticsearchQueryWebClient elasticsearchQueryWebClient;

    public QueryController(ElasticsearchQueryWebClient elasticsearchQueryWebClient) {
        this.elasticsearchQueryWebClient = elasticsearchQueryWebClient;
    }

    @PostMapping("/filter-by-text")
    public Flux<ElasticsearchQueryWebClientResponse> queryByText(@Valid @RequestBody ElasticsearchQueryWebClientRequest request) {

        log.info("Querying by text {}", request.getText());

        Flux<ElasticsearchQueryWebClientResponse> responseFlux = elasticsearchQueryWebClient.searchDocumentsByText(request);
        responseFlux = responseFlux.log();
        // 1 is the size of the buffer (elements are loaded one-by-one)
//        IReactiveDataDriverContextVariable reactiveDataDriverContextVariable = new ReactiveDataDriverContextVariable(responseFlux, 1);
        return responseFlux;
    }
}