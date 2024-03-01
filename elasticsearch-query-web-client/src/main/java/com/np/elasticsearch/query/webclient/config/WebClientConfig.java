package com.np.elasticsearch.query.webclient.config;

import com.np.config.ElasticsearchQueryWebClientConfigData;
import com.np.config.UserConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;


@Configuration
@LoadBalancerClient(
        name = "elasticsearch-query-service",
        configuration = ElasticsearchQueryServiceInstanceListSupplierConfig.class
)
public class WebClientConfig {

    private final ElasticsearchQueryWebClientConfigData elasticsearchQueryWebClientConfigData;
    private final UserConfigData userConfigData;

    public WebClientConfig(ElasticsearchQueryWebClientConfigData elasticsearchQueryWebClientConfigData,
                           UserConfigData userConfigData) {
        this.elasticsearchQueryWebClientConfigData = elasticsearchQueryWebClientConfigData;
        this.userConfigData = userConfigData;
    }

    @LoadBalanced
    @Bean("webClientBuilder")
    WebClient.Builder webClientBuilder() {
        return org.springframework.web.reactive.function.client.WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(userConfigData.getUsername(), userConfigData.getPassword()))
                .baseUrl(elasticsearchQueryWebClientConfigData.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, elasticsearchQueryWebClientConfigData.getContentType())
                .defaultHeader(HttpHeaders.ACCEPT, elasticsearchQueryWebClientConfigData.getAccept())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTcpClient())))
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs()
                        .maxInMemorySize(elasticsearchQueryWebClientConfigData.getMaxInMemorySize())
                ); // if the codecs is not set, we will get an error when we browse large data in browser

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
