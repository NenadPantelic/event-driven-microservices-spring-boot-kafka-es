package com.np.elasticsearch.query.webclient.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticsearchQueryWebClientResponse {

    private String id;
    private Long userId;
    private String text;
    private LocalDateTime createdAt;
}
