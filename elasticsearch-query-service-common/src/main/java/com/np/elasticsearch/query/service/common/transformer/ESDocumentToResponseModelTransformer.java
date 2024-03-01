package com.np.elasticsearch.query.service.common.transformer;


import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.elasticsearch.query.service.common.model.ElasticsearchQueryServiceResponseModel;

import java.util.List;
import java.util.stream.Collectors;

public class ESDocumentToResponseModelTransformer {

    public static ElasticsearchQueryServiceResponseModel transformToResponseModel(TwitterIndexModel twitterIndexModel) {
        if (twitterIndexModel == null) {
            return null;
        }

        return ElasticsearchQueryServiceResponseModel
                .builder()
                .id(twitterIndexModel.getId())
                .userId(twitterIndexModel.getUserId())
                .text(twitterIndexModel.getText())
                .createdAt(twitterIndexModel.getCreatedAt())
                .build();
    }

    public static List<ElasticsearchQueryServiceResponseModel> transformToResponseModelList(List<TwitterIndexModel> twitterIndexModels) {
        if (twitterIndexModels == null) {
            return List.of();
        }

        return twitterIndexModels.stream()
                .map(ESDocumentToResponseModelTransformer::transformToResponseModel)
                .collect(Collectors.toList());
    }
}
