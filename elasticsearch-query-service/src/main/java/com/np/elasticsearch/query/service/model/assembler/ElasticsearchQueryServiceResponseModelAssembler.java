package com.np.elasticsearch.query.service.model.assembler;

import com.np.elastic.model.index.impl.TwitterIndexModel;
import com.np.elasticsearch.query.service.api.ElasticsearchDocumentController;
import com.np.elasticsearch.query.service.model.ElasticsearchQueryServiceResponseModel;
import com.np.elasticsearch.query.service.transformer.ESDocumentToResponseModelTransformer;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticsearchQueryServiceResponseModelAssembler extends
        RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticsearchQueryServiceResponseModel> {

    public ElasticsearchQueryServiceResponseModelAssembler() {
        super(ElasticsearchDocumentController.class, ElasticsearchQueryServiceResponseModel.class);
    }

    @Override
    public ElasticsearchQueryServiceResponseModel toModel(TwitterIndexModel entity) {
        ElasticsearchQueryServiceResponseModel responseModel = ESDocumentToResponseModelTransformer.transformToResponseModel(
                entity
        );

        responseModel.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ElasticsearchDocumentController.class)
                                .getDocumentById(entity.getId()))
                        .withSelfRel()
        );

        responseModel.add(
                WebMvcLinkBuilder.linkTo(ElasticsearchDocumentController.class)
                        .withRel("documents")
        );

        return responseModel;
    }

    public List<ElasticsearchQueryServiceResponseModel> toModels(List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels.stream().map(this::toModel).collect(Collectors.toList());
    }
}


