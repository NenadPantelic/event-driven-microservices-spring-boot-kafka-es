package com.np.elasticsearch.query.service.api;

import com.np.elasticsearch.query.service.common.model.ElasticsearchQueryServiceRequestModel;
import com.np.elasticsearch.query.service.common.model.ElasticsearchQueryServiceResponseModel;
import com.np.elasticsearch.query.service.logic.ElasticsearchQueryService;
import com.np.elasticsearch.query.service.model.ElasticsearchQueryServiceResponseModelV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController// @Controller + @ResponseBody -> used to serialized objects to JSON
@RequestMapping(value = "/api/documents", produces = "application/vnd.api.v1+json")
@Slf4j
public class ElasticsearchDocumentController {

    private final ElasticsearchQueryService elasticsearchQueryService;

    public ElasticsearchDocumentController(ElasticsearchQueryService elasticsearchQueryService) {
        this.elasticsearchQueryService = elasticsearchQueryService;
    }

    // localhost:8183/elasticsearch-query-service/api/v1/documents (context path is changed)
    @Operation(summary = "Get all documents from Elasticsearch.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticsearchQueryServiceResponseModel.class)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
    })
    @GetMapping
    public ResponseEntity<List<ElasticsearchQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticsearchQueryServiceResponseModel> documents = elasticsearchQueryService.getAllDocuments();
        log.info("Elasticsearch returned {} documents", documents.size());
        return ResponseEntity.ok(documents);
    }

    @Operation(summary = "Get document from Elasticsearch by id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticsearchQueryServiceResponseModel.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ElasticsearchQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticsearchQueryServiceResponseModel esDocument = elasticsearchQueryService.getDocumentById(id);
        log.info("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(esDocument);
    }

    @Operation(summary = "Get document from Elasticsearch by id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = "application/vnd.api.v2+json",
                            schema = @Schema(implementation = ElasticsearchQueryServiceResponseModelV2.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Not found."),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public ResponseEntity<ElasticsearchQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id) {
        ElasticsearchQueryServiceResponseModel esDocument = elasticsearchQueryService.getDocumentById(id);
        log.info("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(mapToResponseModelV2(esDocument));
    }

    @Operation(summary = "Get documents from Elasticsearch by filtering by text.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Success",
                    content = @Content(
                            mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticsearchQueryServiceResponseModel.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
    })
    @PostMapping("/query-by-text")
    public ResponseEntity<List<ElasticsearchQueryServiceResponseModel>> getDocumentByText(
            @RequestBody @Valid ElasticsearchQueryServiceRequestModel elasticsearchQueryServiceRequestModel) {
        List<ElasticsearchQueryServiceResponseModel> documents = elasticsearchQueryService.getDocumentsByText(
                elasticsearchQueryServiceRequestModel.getText()
        );
        return ResponseEntity.ok(documents);
    }

    private ElasticsearchQueryServiceResponseModelV2 mapToResponseModelV2(ElasticsearchQueryServiceResponseModel responseModel) {
        ElasticsearchQueryServiceResponseModelV2 responseModelV2 = ElasticsearchQueryServiceResponseModelV2.builder()
                .id(Long.valueOf(responseModel.getId()))
                .userId(responseModel.getUserId())
                .text(responseModel.getText())
                .textV2("Version 2 text")
                .build();

        responseModelV2.add(responseModel.getLinks());
        return responseModelV2;
    }
}