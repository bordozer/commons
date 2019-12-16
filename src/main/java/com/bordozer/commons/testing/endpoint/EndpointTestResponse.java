package com.bordozer.commons.testing.endpoint;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import javax.annotation.CheckForNull;

@Getter
@RequiredArgsConstructor
public final class EndpointTestResponse {
    private final EndpointTestBuilder endpointTestBuilder;

    @CheckForNull
    private HttpStatus responseHttpStatus;
    @CheckForNull
    private String responseContentType;
    @CheckForNull
    private ResponseMatchType responseMatchType;
    private final ResponseHttpHeaders responseHttpHeaders = new ResponseHttpHeaders();

    @CheckForNull
    private String responseBody;

    public EndpointTestResponse hasHttpStatus(final HttpStatus responseHttpStatus) {
        this.responseHttpStatus = responseHttpStatus;
        return this;
    }

    public EndpointTestResponse hasOkHttpStatus() {
        this.responseHttpStatus = HttpStatus.OK;
        return this;
    }

    public EndpointTestResponse hasContentType(final MediaType contentType) {
        this.responseContentType = contentType.toString();
        return this;
    }

    public EndpointTestResponse hasContentType(final String contentType) {
        this.responseContentType = contentType;
        return this;
    }

    public EndpointTestResponse hasHttpHeaderWithParticularValue(final String header, final String value) {
        Assert.notNull(value, "Header value must not be null. Use hasNoHttpHeader() instead of passing null here");
        Assert.isTrue(!responseHttpHeaders.exists(header), String.format("Http header '%s' has already been added for checking", header));
        this.responseHttpHeaders.add(ResponseHttpHeader.existsWithParticularValue(header, value));
        return this;
    }

    public EndpointTestResponse hasHttpHeaderWithAnyValue(final String header) {
        Assert.isTrue(!responseHttpHeaders.exists(header), String.format("Http header '%s' has already been added for checking", header));
        this.responseHttpHeaders.add(ResponseHttpHeader.existsWithAnyValue(header));
        return this;
    }

    public EndpointTestResponse doesNotHaveHttpHeader(final String header) {
        Assert.isTrue(!responseHttpHeaders.exists(header), String.format("Http header '%s' has already been added for checking", header));
        this.responseHttpHeaders.add(ResponseHttpHeader.doesNotExists(header));
        return this;
    }

    public EndpointTestResponse hasJsonContentType() {
        this.responseContentType = MediaType.APPLICATION_JSON_UTF8.toString();
        return this;
    }

    public EndpointTestResponse hasBodyContains(@CheckForNull final String text) {
        setResponseBody(text, ResponseMatchType.CONTAINS);
        return this;
    }

    public EndpointTestResponse hasBodyEqualsTo(@CheckForNull final String response) {
        setResponseBody(response, ResponseMatchType.EQUALS_TO);
        return this;
    }

    public EndpointTestResponse hasBodyJson(@CheckForNull final String responseJson) {
        setResponseBody(responseJson, ResponseMatchType.JSON);
        return this;
    }

    private void setResponseBody(@CheckForNull final String responseAsString, final ResponseMatchType responseMatchType) {
        this.responseBody = responseAsString;
        this.responseMatchType = responseMatchType;
    }

    public EndpointTestBuilder end() {
        return endpointTestBuilder;
    }
}
