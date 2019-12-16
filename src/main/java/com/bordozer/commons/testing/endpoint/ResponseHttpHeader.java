package com.bordozer.commons.testing.endpoint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseHttpHeader {
    private final String header;
    private final String value;
    private final HeaderPresence headerPresence;

    public static ResponseHttpHeader existsWithParticularValue(final String header, String value) {
        return new ResponseHttpHeader(header, value, HeaderPresence.EXISTS_WITH_PARTICULAR_VALUE);
    }

    public static ResponseHttpHeader existsWithAnyValue(final String header) {
        return new ResponseHttpHeader(header, null, HeaderPresence.EXISTS_WITH_ANY_VALUE);
    }

    public static ResponseHttpHeader doesNotExists(final String header) {
        return new ResponseHttpHeader(header, null, HeaderPresence.DOES_NOT_EXISTS);
    }

    public enum HeaderPresence {
        EXISTS_WITH_PARTICULAR_VALUE,
        EXISTS_WITH_ANY_VALUE,
        DOES_NOT_EXISTS
    }
}
