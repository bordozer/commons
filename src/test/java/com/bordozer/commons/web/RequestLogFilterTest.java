package com.bordozer.commons.web;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RequestLogFilterTest {

    public static final WebLogger LOGGER = mock(WebLogger.class);

    @Test
    void shouldLogRequestAndResponse() throws ServletException, IOException {
        // given
        final RequestLogFilter filter = new RequestLogFilter(true, true, LOGGER);
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/some/path/");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain chain = new MockFilterChain();

        // when
        filter.doFilterInternal(request, response, chain);

        // then
        verify(LOGGER).logRequest(any(ContentCachingRequestWrapper.class));
        verify(LOGGER).logResponse(any(ContentCachingResponseWrapper.class));
    }

    @Test
    void shouldNotLogRequestAndResponse() throws ServletException, IOException {
        // given
        final RequestLogFilter filter = new RequestLogFilter(false, false, LOGGER);
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/some/path/");

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain chain = new MockFilterChain();

        // when
        filter.doFilterInternal(request, response, chain);

        // then
        verify(LOGGER, never()).logRequest(any(ContentCachingRequestWrapper.class));
        verify(LOGGER, never()).logResponse(any(ContentCachingResponseWrapper.class));
    }
}
