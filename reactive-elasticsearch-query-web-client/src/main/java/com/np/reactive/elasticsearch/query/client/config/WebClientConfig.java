package com.np.reactive.elasticsearch.query.client.config;

import com.np.config.ElasticsearchQueryWebClientConfigData;
import com.np.config.UserConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

public class WebClientConfig {

    private final ElasticsearchQueryWebClientConfigData elasticsearchQueryWebClientConfigData;

    public WebClientConfig(ElasticsearchQueryWebClientConfigData elasticsearchQueryWebClientConfigData,
                           UserConfigData userConfigData) {
        this.elasticsearchQueryWebClientConfigData = elasticsearchQueryWebClientConfigData;
    }

    @Bean("webClient")
    WebClient webClientBuilder() {
        return WebClient.builder()
                .baseUrl(elasticsearchQueryWebClientConfigData.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, elasticsearchQueryWebClientConfigData.getContentType())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTcpClient())))
                .build();
    }

    private TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, elasticsearchQueryWebClientConfigData.getConnectTimeoutMs())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(elasticsearchQueryWebClientConfigData.getReadTimeoutMs(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(elasticsearchQueryWebClientConfigData.getWriteTimeoutMs(), TimeUnit.MILLISECONDS));
                });
    }
}
