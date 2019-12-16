package com.bordozer.commons.web;

import com.bordozer.commons.utils.LoggableJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            logRequest(requestWrapper);
            logResponse(responseWrapper);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(final ContentCachingRequestWrapper requestWrapper) {
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

    private void logResponse(final ContentCachingResponseWrapper responseWrapper) {
        final HttpStatus httpStatus = HttpStatus.valueOf(responseWrapper.getStatus());
        final String contentType = responseWrapper.getContentType();
        final String headers = LoggableJson.of(responseWrapper.getHeaderNames().stream().distinct()
                .collect(Collectors.toMap(head -> head, responseWrapper::getHeader))).toString();
        final String body = getBody(responseWrapper.getContentAsByteArray());
        log.info("Response: httpStatus=\"{}\", contentType=\"{}\", headers=\"{}\", body=\"{}\"",
                httpStatus, contentType, headers, body);
    }

    private String getBody(byte[] contentAsByteArray) {
        return new String(contentAsByteArray)
                .replaceAll(System.lineSeparator(), "")
                .replaceAll("\t", "");
    }
}
