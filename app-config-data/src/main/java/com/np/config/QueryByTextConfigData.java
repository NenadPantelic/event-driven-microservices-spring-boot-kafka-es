package com.np.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("elasticsearch-query-web-client.query-by-text")
public class QueryByTextConfigData {

    private String method;
    private String accept;
    private String uri;
}
