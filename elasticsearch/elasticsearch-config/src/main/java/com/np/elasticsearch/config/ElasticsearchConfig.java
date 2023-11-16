package com.np.elasticsearch.config;

import com.np.config.ElasticsearchConfigData;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Configuration
// required to scan and find elasticsearch repositories
@EnableElasticsearchRepositories(basePackages = "com.np.elasticsearch.client.repository")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private final ElasticsearchConfigData elasticsearchConfigData;

    public ElasticsearchConfig(ElasticsearchConfigData elasticsearchConfigData) {
        this.elasticsearchConfigData = elasticsearchConfigData;
    }

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        UriComponents serverUri = UriComponentsBuilder.fromHttpUrl(elasticsearchConfigData.getConnectionUrl()).build();

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(
                        Objects.requireNonNull(serverUri.getHost()),
                        serverUri.getPort(),
                        serverUri.getScheme()
                )).setRequestConfigCallback(requestConfigBuilder ->
                        requestConfigBuilder.setConnectTimeout(elasticsearchConfigData.getConnectTimeoutMs())
                                .setSocketTimeout(elasticsearchConfigData.getSocketTimeoutMs()))
        );
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
