package com.bordozer.commons.web;

import com.bordozer.commons.utils.LoggableJson;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.annotation.CheckForNull;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class WebLogger {

    WebLogger() {
    }

    void logRequest(final RequestLogData requestLogData) {
        final String headers = LoggableJson.of(requestLogData.getHttpHeaders().keySet().stream()
                .collect(Collectors.toMap(head -> head, head -> requestLogData.getHttpHeaders().get(head)))).toString();
        final String parameters = LoggableJson.of(requestLogData.getHttpParameters()).toString();

        log.info("Request: URI=\"{}\", method=\"{}\", contentType=\"{}\", headers=\"{}\", queryString=\"{}\", parameters=\"{}\", body=\"{}\"",
                requestLogData.getUri(), requestLogData.getHttpMethod(), requestLogData.getContentType(), headers,
                requestLogData.getQueryString(), parameters, requestLogData.getBody());
    }

    void logResponse(final ResponseLogData responseLogData) {
        final String headers = LoggableJson.of(responseLogData.getHttpHeaders().keySet().stream().distinct()
                .collect(Collectors.toMap(head -> head, head -> responseLogData.getHttpHeaders().get(head)))).toString();

        log.info("Response: httpStatus=\"{}\", contentType=\"{}\", headers=\"{}\", body=\"{}\"",
                responseLogData.getResponseStatus(), responseLogData.getContentType(), headers, responseLogData.getBody());
    }

    @Getter
    @Builder
    @ToString
    public static class RequestLogData {
        @NonNull
        private final String uri;
        @NonNull
        private final HttpMethod httpMethod;
        @CheckForNull
        private final String contentType;
        @NonNull
        private final Map<String, String> httpHeaders;
        @CheckForNull
        private final String queryString;
        @CheckForNull
        private final Map<String, String[]> httpParameters;
        @CheckForNull
        private final String body;
    }

    @Getter
    @Builder
    @ToString
    public static class ResponseLogData {
        @NonNull
        private final HttpStatus responseStatus;
        @CheckForNull
        private final String contentType;
        @NonNull
        private final Map<String, String> httpHeaders;
        @CheckForNull
        private final String body;
    }
}
