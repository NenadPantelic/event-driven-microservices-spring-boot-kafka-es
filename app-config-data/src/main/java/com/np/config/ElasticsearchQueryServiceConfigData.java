package com.np.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("elasticsearch-query-service")
public class ElasticsearchQueryServiceConfigData {

    private String version;
}
