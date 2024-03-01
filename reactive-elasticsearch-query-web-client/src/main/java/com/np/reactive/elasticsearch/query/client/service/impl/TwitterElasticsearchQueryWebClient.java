package com.np.reactive.elasticsearch.query.client.service.impl;

import com.np.config.QueryByTextConfigData;
import com.np.elasticsearch.query.webclient.common.exception.ElasticsearchQueryWebClientException;
import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientRequest;
import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientResponse;
import com.np.reactive.elasticsearch.query.client.service.ElasticsearchQueryWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class TwitterElasticsearchQueryWebClient implements ElasticsearchQueryWebClient {

    private final WebClient.Builder webClientBuilder;
    private final QueryByTextConfigData queryByTextConfigData;

    public TwitterElasticsearchQueryWebClient(@Qualifier("webClient") WebClient.Builder webClientBuilder,
                                              QueryByTextConfigData queryByTextConfigData) {
        this.webClientBuilder = webClientBuilder;
        this.queryByTextConfigData = queryByTextConfigData;
    }

    @Override
    public Flux<ElasticsearchQueryWebClientResponse> searchDocumentsByText(ElasticsearchQueryWebClientRequest request) {
        log.info("Querying by text {}", request.getText());
        return getWebClient(request).bodyToFlux(ElasticsearchQueryWebClientResponse.class);
    }

    private WebClient.ResponseSpec getWebClient(ElasticsearchQueryWebClientRequest request) {
        return webClientBuilder
                .build()
                .method(HttpMethod.valueOf(queryByTextConfigData.getMethod()))
                .uri(queryByTextConfigData.getUri())
                .accept(MediaType.valueOf(queryByTextConfigData.getAccept()))
                .body(BodyInserters.fromPublisher(Mono.just(request), createParameterizedTypeReference()))
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, clientResponse -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.just(new ElasticsearchQueryWebClientException(clientResponse.statusCode().getReasonPhrase())))
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.just(new ElasticsearchQueryWebClientException(clientResponse.statusCode().getReasonPhrase())));
    }

    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<T>() {
        };
    }
}
