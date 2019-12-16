package com.bordozer.commons.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String LOG_TRACE_ID = "traceId";
    public static final String HTTP_HEADER_TRACE_ID = "x-trace-id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String traceId = request.getHeader(HTTP_HEADER_TRACE_ID);
            if (StringUtils.isEmpty(traceId)) {
                traceId = UUID.randomUUID().toString();
                log.info("TraceId cannot be obtained from http header. Generated new \"{}\"", traceId);
            } else {
                log.info("TraceId \"{}\" has been got from http header \"{}\"", traceId, HTTP_HEADER_TRACE_ID);
            }

            MDC.put(LOG_TRACE_ID, traceId);

            filterChain.doFilter(request, response);

            if (StringUtils.isEmpty(response.getHeader(HTTP_HEADER_TRACE_ID))) {
                response.addHeader(HTTP_HEADER_TRACE_ID, traceId);
            }

            log.info("TraceId has been added to response http header \"{}\"", HTTP_HEADER_TRACE_ID);
        } finally {
            MDC.clear();
        }
    }
}
