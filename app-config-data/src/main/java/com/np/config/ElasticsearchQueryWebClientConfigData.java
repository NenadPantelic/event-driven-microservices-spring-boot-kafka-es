package com.np.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Data
@Configuration
@ConfigurationProperties("elasticsearch-query-web-client")
public class ElasticsearchQueryWebClientConfigData {

    private Integer connectTimeoutMs;
    private Integer readTimeoutMs;
    private Integer writeTimeoutMs;
    private Integer maxInMemorySize;
    private String contentType;
    private String accept;
    private String serviceId;
    private String baseUrl;
    private List<Instance> instances;

    @Data
    public static class Instance {
        private String id;
        private String host;
        private Integer port;
    }
}
