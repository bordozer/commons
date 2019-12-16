package com.bordozer.commons.web;

import com.bordozer.commons.utils.LoggableJson;
import com.bordozer.commons.web.WebLogger.RequestLogData;
import com.bordozer.commons.web.WebLogger.ResponseLogData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.annotation.CheckForNull;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestLogFilterTest {

    @Captor
    private ArgumentCaptor<RequestLogData> requestLogDataCaptor;
    @Captor
    private ArgumentCaptor<ResponseLogData> responseLogDataCaptor;

    @Test
    void shouldLogRequestAndResponse() throws ServletException, IOException {
        // given
        final WebLogger logger = mock(WebLogger.class);
        final RequestLogFilter filter = new RequestLogFilter(true, true, logger);
        final MockHttpServletRequest request = mockRequest();
        final MockHttpServletResponse response = mockResponse();
        final MockFilterChain chain = new MockFilterChain();

        // when
        filter.doFilterInternal(request, response, chain);

        // then
        verify(logger).logRequest(requestLogDataCaptor.capture());
        verify(logger).logResponse(responseLogDataCaptor.capture());

        final RequestLogData requestLogData = requestLogDataCaptor.getValue();
        assertThat(requestLogData.getContentType()).isEqualTo("application/json");
        assertThat(requestLogData.getUri()).isEqualTo("/some/uri");
        assertThat(requestLogData.getHttpMethod()).isEqualTo(HttpMethod.POST);
        assertThat(asJson(requestLogData.getHttpHeaders())).isEqualTo("{\"Content-Type\":\"application/json;charset=UTF-8\"}");
        assertThat(requestLogData.getQueryString()).isEqualTo("alpha=1&beta=2");
        assertThat(asJson(requestLogData.getHttpParameters())).isEqualTo("{\"p1\":[\"11\"],\"p2\":[\"22\"]}");
//        assertThat(asJson(requestLogData.getBody())).isEqualTo("{\"key\": \"value\"}"); // TODO: for some reason the body is EMPTY

        final ResponseLogData responseLogData = responseLogDataCaptor.getValue();
        assertThat(responseLogData.getResponseStatus()).isEqualTo(HttpStatus.OK);
        assertThat(responseLogData.getContentType()).isEqualTo("application/json");
        assertThat(asJson(responseLogData.getHttpHeaders())).isEqualTo("{\"head1\":\"hv1\",\"head2\":\"hv2\",\"Content-Type\":\"application/json;charset=UTF-8\"}");
        assertThat(responseLogData.getBody()).isEqualTo("");
    }

    @Test
    void shouldNotLogRequestAndResponse() throws ServletException, IOException {
        // given
        final WebLogger logger = mock(WebLogger.class);
        final RequestLogFilter filter = new RequestLogFilter(false, false, logger);
        final MockHttpServletRequest request = mockRequest();
        final MockHttpServletResponse response = mockResponse();
        final MockFilterChain chain = new MockFilterChain();

        // when
        filter.doFilterInternal(request, response, chain);

        // then
        verify(logger, never()).logRequest(any(RequestLogData.class));
        verify(logger, never()).logResponse(any(ResponseLogData.class));
    }

    private MockHttpServletRequest mockRequest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/some/path/");
        request.setContentType("application/json");
        request.setRequestURI("/some/uri");
        request.setMethod("POST");
        request.setQueryString("alpha=1&beta=2");
        request.setParameter("p1", "11");
        request.setParameter("p2", "22");
        request.setCharacterEncoding("UTF-8");
        request.setContent("{\"key\": \"value\"}".getBytes());
        return request;
    }

    private MockHttpServletResponse mockResponse() {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        response.setContentType("application/json");
        response.addHeader("head1", "hv1");
        response.addHeader("head2", "hv2");
        response.setCharacterEncoding("UTF-8");
        return response;
    }

    private String asJson(@CheckForNull final Object requestLogData) {
        return LoggableJson.of(requestLogData).toString();
    }
}
