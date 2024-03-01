package com.np.elasticsearch.query.webclient.service.impl;

import com.np.config.QueryByTextConfigData;

import com.np.elasticsearch.query.webclient.common.exception.ElasticsearchQueryWebClientException;
import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientRequest;
import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientResponse;
import com.np.elasticsearch.query.webclient.service.ElasticsearchQueryWebClient;
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
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class TwitterElasticsearchQueryWebClient implements ElasticsearchQueryWebClient {

    private final WebClient.Builder webClientBuilder;
    private final QueryByTextConfigData queryByTextConfigData;

    // Qualifier is used here because there is a default implementation that Spring lib provides
    public TwitterElasticsearchQueryWebClient(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder,
                                              QueryByTextConfigData queryByTextConfigData) {
        this.webClientBuilder = webClientBuilder;
        this.queryByTextConfigData = queryByTextConfigData;
    }

    @Override
    public List<ElasticsearchQueryWebClientResponse> searchByText(ElasticsearchQueryWebClientRequest request) {
        log.info("Querying by text {}", request.getText());
        return getWebClient(request)
                .bodyToFlux(ElasticsearchQueryWebClientResponse.class)
                .collectList()
                .block(); // make a blocking call
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
    // mono - single object
    // flux - list of objects
}
