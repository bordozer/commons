package com.bordozer.commons.testing.endpoint;

import lombok.Getter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Getter
public class ResponseHttpHeaders {
    private final List<ResponseHttpHeader> responseHttpHeaders = newArrayList();

    public void add(final ResponseHttpHeader responseHttpHeader) {
        responseHttpHeaders.add(responseHttpHeader);
    }

    public boolean exists(final String header) {
        return responseHttpHeaders.stream()
                .anyMatch(h -> h.getHeader().equals(header));
    }
}
