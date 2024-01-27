package com.np.elasticsearch.query.service.api;

import com.np.elasticsearch.query.service.logic.ElasticsearchQueryService;
import com.np.elasticsearch.query.service.model.ElasticsearchQueryServiceRequestModel;
import com.np.elasticsearch.query.service.model.ElasticsearchQueryServiceResponseModel;
import com.np.elasticsearch.query.service.model.ElasticsearchQueryServiceResponseModelV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController// @Controller + @ResponseBody -> used to serialized objects to JSON
@RequestMapping("/api")
@Slf4j
public class ElasticsearchDocumentController {

    private final ElasticsearchQueryService elasticsearchQueryService;

    public ElasticsearchDocumentController(ElasticsearchQueryService elasticsearchQueryService) {
        this.elasticsearchQueryService = elasticsearchQueryService;
    }

    // localhost:8183/elasticsearch-query-service/api/v1/documents (context path is changed)
    @GetMapping("/v1/documents")
    public ResponseEntity<List<ElasticsearchQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticsearchQueryServiceResponseModel> documents = elasticsearchQueryService.getAllDocuments();
        log.info("Elasticsearch returned {} documents", documents.size());
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/v1/documents/{id}")
    public ResponseEntity<ElasticsearchQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticsearchQueryServiceResponseModel esDocument = elasticsearchQueryService.getDocumentById(id);
        log.info("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(esDocument);
    }

    @GetMapping("/v2/documents/{id}")
    public ResponseEntity<ElasticsearchQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id) {
        ElasticsearchQueryServiceResponseModel esDocument = elasticsearchQueryService.getDocumentById(id);
        log.info("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(mapToV2Model(esDocument));
    }

    @PostMapping("/v1/documents")
    public ResponseEntity<List<ElasticsearchQueryServiceResponseModel>> getDocumentByText(
            @RequestBody @Valid ElasticsearchQueryServiceRequestModel elasticsearchQueryServiceRequestModel) {
        List<ElasticsearchQueryServiceResponseModel> documents = elasticsearchQueryService.getDocumentsByText(
                elasticsearchQueryServiceRequestModel.getText()
        );
        return ResponseEntity.ok(documents);
    }

    private ElasticsearchQueryServiceResponseModelV2 mapToV2Model(ElasticsearchQueryServiceResponseModel responseModel) {
        ElasticsearchQueryServiceResponseModelV2 responseModelV2 = ElasticsearchQueryServiceResponseModelV2.builder()
                .id(Long.valueOf(responseModel.getId()))
                .userId(responseModel.getUserId())
                .text(responseModel.getText())
                .createdAt(responseModel.getCreatedAt())
                .build();

        responseModelV2.add(responseModel.getLinks());
        return responseModelV2;
    }
}