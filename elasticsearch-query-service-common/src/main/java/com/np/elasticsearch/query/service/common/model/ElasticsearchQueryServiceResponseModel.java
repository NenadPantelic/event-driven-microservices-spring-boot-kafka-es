package com.np.elasticsearch.query.service.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ElasticsearchQueryServiceResponseModel extends RepresentationModel<ElasticsearchQueryServiceResponseModel> {

    private String id;
    private Long userId;
    private String text;
    private LocalDateTime createdAt;
}
