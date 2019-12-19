package com.bordozer.commons.web;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.annotation.CheckForNull;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class WebLogger {

    public void logRequest(final RequestLogData requestLogData) {
        final String headers = extractHeader(requestLogData.getHttpHeaders());
        final String parameters = extractParameters(requestLogData.getHttpParameters());

        log.info("Request: URI=\"{}\", method=\"{}\", contentType=\"{}\", headers=\"{}\", queryString=\"{}\", parameters=\"{}\", body=\"{}\"",
                requestLogData.getUri(), requestLogData.getHttpMethod(), requestLogData.getContentType(), headers,
                requestLogData.getQueryString(), parameters, requestLogData.getBody());
    }

    public void logResponse(final ResponseLogData responseLogData) {
        final String headers = extractHeader(responseLogData.getHttpHeaders());

        log.info("Response: httpStatus=\"{}\", contentType=\"{}\", headers=\"{}\", body=\"{}\"",
                responseLogData.getResponseStatus(), responseLogData.getContentType(), headers, responseLogData.getBody());
    }

    private static String extractHeader(final Map<String, String> httpHeaders) {
        final Set<String> headerNames = httpHeaders.keySet();
        return headerNames.stream()
                .sorted()
                .map(header -> String.format("%s=\"%s\"", header, httpHeaders.get(header)))
                .collect(Collectors.joining(","));
    }

    private static String extractParameters(@CheckForNull final Map<String, String[]> parameterMap) {
        if (parameterMap == null) {
            return StringUtils.EMPTY;
        }
        return parameterMap.keySet().stream()
                .map(param -> String.format("%s=[%s]", param, Arrays.stream(parameterMap.get(param))
                        .map(pv -> String.format("\"%s\"", pv))
                        .collect(Collectors.joining(",")))
                )
                .collect(Collectors.joining(","));
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
