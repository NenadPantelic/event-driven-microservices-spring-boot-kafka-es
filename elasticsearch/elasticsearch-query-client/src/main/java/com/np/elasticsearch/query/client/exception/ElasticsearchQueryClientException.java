package com.np.elasticsearch.query.client.exception;

public class ElasticsearchQueryClientException extends RuntimeException {
    public ElasticsearchQueryClientException() {
    }

    public ElasticsearchQueryClientException(String message) {
        super(message);
    }

    public ElasticsearchQueryClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
