package com.np.elasticsearch.query.webclient.common.exception;

public class ElasticsearchQueryWebClientException extends RuntimeException {

    public ElasticsearchQueryWebClientException() {
    }

    public ElasticsearchQueryWebClientException(String message) {
        super(message);
    }

    public ElasticsearchQueryWebClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
