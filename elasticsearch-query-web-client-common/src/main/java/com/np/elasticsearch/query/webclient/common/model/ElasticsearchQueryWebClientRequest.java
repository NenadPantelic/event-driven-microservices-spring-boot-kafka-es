package com.np.elasticsearch.query.webclient.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticsearchQueryWebClientRequest {

    private String id;
    @NotBlank
    private String text;
}
