package com.np.reactive.elasticsearch.query.service.api;

import com.np.elasticsearch.query.service.common.model.ElasticsearchQueryServiceRequestModel;
import com.np.elasticsearch.query.service.common.model.ElasticsearchQueryServiceResponseModel;
import com.np.reactive.elasticsearch.query.service.logic.ElasticsearchQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/documents")
public class ElasticsearchDocumentController {

    private final ElasticsearchQueryService elasticsearchQueryService;

    public ElasticsearchDocumentController(ElasticsearchQueryService elasticsearchQueryService) {
        this.elasticsearchQueryService = elasticsearchQueryService;
    }

    @PostMapping(value = "/filter-by-text",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE) // event stream output (consumed in chunks)
    public Flux<ElasticsearchQueryServiceResponseModel> filterDocumentsByText(
            @RequestBody @Valid ElasticsearchQueryServiceRequestModel requestModel
    ) {
        Flux<ElasticsearchQueryServiceResponseModel> response = elasticsearchQueryService.getDocumentsByText(requestModel.getText());
        response = response.log(); //  trace the log
        log.info("Returning from query reactive service for text {}!", requestModel.getText());
        return response;

        // when we start this service, reactor.Netty server will go up to serve event streams
    }
}
