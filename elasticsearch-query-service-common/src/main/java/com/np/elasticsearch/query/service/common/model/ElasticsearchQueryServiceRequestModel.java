package com.np.elasticsearch.query.service.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ElasticsearchQueryServiceRequestModel {

    private String id;
    @NotEmpty
    private String text;
}
