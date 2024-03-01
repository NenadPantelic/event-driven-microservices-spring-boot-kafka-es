package com.np.elasticsearch.query.webclient.api;

import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientRequest;
import com.np.elasticsearch.query.webclient.common.model.ElasticsearchQueryWebClientResponse;
import com.np.elasticsearch.query.webclient.service.ElasticsearchQueryWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class QueryController {

    private final ElasticsearchQueryWebClient elasticsearchQueryWebClient;

    private final int port;

    public QueryController(ElasticsearchQueryWebClient elasticsearchQueryWebClient,
                           @Value("${server.port}") int port) {
        this.elasticsearchQueryWebClient = elasticsearchQueryWebClient;
        this.port = port;
    }

//    @GetMapping
//    public String index() {
//        return "index";
//    }
//
//    @GetMapping("/error")
//    public String error() {
//        return "error";
//    }
//
//    @GetMapping("/home")
//    public String home(Model model) {
//        // ui.Model is a class used to communicate between backend and thymeleaf templates
//        model.addAttribute(
//                "elasticsearchQueryWebClientRequest",
//                ElasticsearchQueryWebClientRequest.builder().build()
//        );
//        return "home";
//    }

//    @PostMapping("/query-by-text")
//    public String queryByText(@Valid ElasticsearchQueryWebClientRequest request,
//                              Model model) {
//
//        log.info("Querying by text {} on port {}", request.getText(), port);
//        List<ElasticsearchQueryWebClientResponse> responseList = elasticsearchQueryWebClient.searchByText(request);
//
//        model.addAttribute("elasticsearchQueryWebClientResponses", responseList);
//        model.addAttribute("searchText", request.getText());
//        model.addAttribute("elasticsearchQueryWebClientRequest", ElasticsearchQueryWebClientRequest.builder().build());
//
//        return "home";
//    }

    @PostMapping("/query-by-text")
    public List<ElasticsearchQueryWebClientResponse> queryByText(@Valid @RequestBody ElasticsearchQueryWebClientRequest request) {

        log.info("Querying by text {} on port {}", request.getText(), port);
        return elasticsearchQueryWebClient.searchByText(request);
    }

//    @ModelAttribute("elasticsearchQueryWebClientRequest")
//    public ElasticsearchQueryWebClientRequest loadEmptyModelBean() {
//        return ElasticsearchQueryWebClientRequest.builder().build();
//    }
}
