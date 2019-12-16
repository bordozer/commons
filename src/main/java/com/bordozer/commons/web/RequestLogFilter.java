package com.bordozer.commons.web;

import com.bordozer.commons.utils.LoggableJson;
import lombok.RequiredArgsConstructor;
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
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class RequestLogFilter extends OncePerRequestFilter {
    private final boolean logRequest;
    private final boolean logResponse;
    private final WebLogger webLogger;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
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

    private void logRequest(ContentCachingRequestWrapper requestWrapper) {
        if (!logRequest) {
            return;
        }
        final String contentType = requestWrapper.getContentType();
        final HttpMethod method = HttpMethod.valueOf(requestWrapper.getMethod());
        final String requestURI = requestWrapper.getRequestURI();
        final String queryString = requestWrapper.getQueryString();
        final Map<String, String> headers = Collections.list(requestWrapper.getHeaderNames()).stream()
                .collect(Collectors.toMap(head -> head, requestWrapper::getHeader));
        final String body = getBody(requestWrapper.getContentAsByteArray());
        final Map<String, String[]> parameters = requestWrapper.getParameterMap();

        webLogger.logRequest(WebLogger.RequestLogData.builder()
                .uri(requestURI)
                .httpMethod(method)
                .contentType(contentType)
                .httpHeaders(headers)
                .queryString(queryString)
                .httpParameters(parameters)
                .body(body)
        .build());
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper) {
        if (!logResponse) {
            return;
        }
        final HttpStatus httpStatus = HttpStatus.valueOf(responseWrapper.getStatus());
        final String contentType = responseWrapper.getContentType();
        final Map<String, String> headers = responseWrapper.getHeaderNames().stream().distinct()
                .collect(Collectors.toMap(head -> head, responseWrapper::getHeader));
        final String body = getBody(responseWrapper.getContentAsByteArray());

        webLogger.logResponse(WebLogger.ResponseLogData.builder()
                .responseStatus(httpStatus)
                .contentType(contentType)
                .httpHeaders(headers)
                .body(body)
                .build());
    }

    private static String getBody(byte[] contentAsByteArray) {
        return new String(contentAsByteArray)
                .replaceAll(System.lineSeparator(), "")
                .replaceAll("\t", "");
    }
}
