package com.bordozer.commons.testing.endpoint;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.annotation.CheckForNull;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class EndpointTestRequest {
    private final EndpointTestBuilder endpointTestBuilder;

    private MediaType requestContentType = MediaType.APPLICATION_JSON_UTF8;
    private final HttpHeaders requestHttpHeaders = new HttpHeaders();
    private final Map<String, String> requestHttpParameters = new HashMap<>();
    @CheckForNull
    private String requestJson;

    private final EndpointTestResponse expectedResponse;

    EndpointTestRequest(final EndpointTestBuilder endpointTestBuilder) {
        this.endpointTestBuilder = endpointTestBuilder;
        this.expectedResponse = new EndpointTestResponse(endpointTestBuilder);
    }

    public EndpointTestRequest withBodyJson(final String requestJson) {
        this.requestJson = requestJson;
        return this;
    }

    public EndpointTestRequest withContentType(final MediaType requestContentType) {
        this.requestContentType = requestContentType;
        return this;
    }

    public EndpointTestRequest withHttpHeader(final String header, @CheckForNull final String value) {
        this.requestHttpHeaders.add(header, value);
        return this;
    }

    public EndpointTestRequest withHttpParameter(final String name, @CheckForNull final String value) {
        this.requestHttpParameters.put(name, value);
        return this;
    }

    public EndpointTestResponse thenResponse() {
        return expectedResponse;
    }

    public EndpointTestBuilder thenResponseSuccessWithJsonBody(final String responseJson) {
        expectedResponse.hasHttpStatus(HttpStatus.OK);
        expectedResponse.hasContentType(MediaType.APPLICATION_JSON_UTF8);
        expectedResponse.hasBodyJson(responseJson);

        return expectedResponse.end();
    }
}
