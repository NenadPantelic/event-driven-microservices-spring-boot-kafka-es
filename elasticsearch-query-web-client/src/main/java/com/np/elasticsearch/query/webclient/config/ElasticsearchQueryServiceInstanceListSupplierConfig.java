package com.np.elasticsearch.query.webclient.config;

import com.np.config.ElasticsearchQueryWebClientConfigData;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Primary
public class ElasticsearchQueryServiceInstanceListSupplierConfig implements ServiceInstanceListSupplier {

    private final ElasticsearchQueryWebClientConfigData elasticsearchQueryWebClientConfigData;

    public ElasticsearchQueryServiceInstanceListSupplierConfig(ElasticsearchQueryWebClientConfigData elasticsearchQueryWebClientConfigData) {
        this.elasticsearchQueryWebClientConfigData = elasticsearchQueryWebClientConfigData;
    }

    @Override
    public String getServiceId() {
        return elasticsearchQueryWebClientConfigData.getServiceId();
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(elasticsearchQueryWebClientConfigData.getInstances()
                .stream()
                .map(instance -> new DefaultServiceInstance(
                        instance.getId(),
                        getServiceId(),
                        instance.getHost(),
                        instance.getPort(),
                        false
                )).collect(Collectors.toList()));
    }
}
