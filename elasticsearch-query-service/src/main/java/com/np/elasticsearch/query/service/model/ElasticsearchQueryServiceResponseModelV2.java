package com.np.elasticsearch.query.service.model;


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
public class ElasticsearchQueryServiceResponseModelV2 extends RepresentationModel<ElasticsearchQueryServiceResponseModel> {

    private Long id;
    private Long userId;
    private String text;
    private String textV2;
}
