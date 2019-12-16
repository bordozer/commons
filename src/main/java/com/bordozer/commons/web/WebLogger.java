package com.bordozer.commons.web;

import com.bordozer.commons.utils.LoggableJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
public class WebLogger {

    WebLogger() {
    }

    void logRequest(final ContentCachingRequestWrapper requestWrapper) {
        final String contentType = requestWrapper.getContentType();
        final HttpMethod method = HttpMethod.valueOf(requestWrapper.getMethod());
        final String requestURI = requestWrapper.getRequestURI();
        final String queryString = requestWrapper.getQueryString();
        final String headers = LoggableJson.of(Collections.list(requestWrapper.getHeaderNames()).stream()
                .collect(Collectors.toMap(head -> head, requestWrapper::getHeader))).toString();
        final String body = getBody(requestWrapper.getContentAsByteArray());
        final String parameters = LoggableJson.of(requestWrapper.getParameterMap()).toString();
        log.info("Request: URI=\"{}\", method=\"{}\", contentType=\"{}\", headers=\"{}\", queryString=\"{}\", parameters=\"{}\", body=\"{}\"",
                requestURI, method, contentType, headers, queryString, parameters, body);
    }

    void logResponse(final ContentCachingResponseWrapper responseWrapper) {
        final HttpStatus httpStatus = HttpStatus.valueOf(responseWrapper.getStatus());
        final String contentType = responseWrapper.getContentType();
        final String headers = LoggableJson.of(responseWrapper.getHeaderNames().stream().distinct()
                .collect(Collectors.toMap(head -> head, responseWrapper::getHeader))).toString();
        final String body = getBody(responseWrapper.getContentAsByteArray());
        log.info("Response: httpStatus=\"{}\", contentType=\"{}\", headers=\"{}\", body=\"{}\"",
                httpStatus, contentType, headers, body);
    }

    private static String getBody(byte[] contentAsByteArray) {
        return new String(contentAsByteArray)
                .replaceAll(System.lineSeparator(), "")
                .replaceAll("\t", "");
    }
}
