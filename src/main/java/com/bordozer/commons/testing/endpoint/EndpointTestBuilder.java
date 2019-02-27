package com.bordozer.commons.testing.endpoint;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EndpointTestBuilder {
    private final String url;

    private final EndpointTestRequest request = new EndpointTestRequest(this);

    public static EndpointTestBuilder testEndpoint(final String url) {
        return new EndpointTestBuilder(url);
    }

    String getUrl() {
        return url;
    }

    public EndpointTestRequest whenRequest() {
        return request;
    }
}
