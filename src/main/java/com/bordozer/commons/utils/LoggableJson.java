package com.bordozer.commons.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

import javax.annotation.CheckForNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggableJson {

    @CheckForNull
    private final Object value;

    private final Function<Object, String> toJsonFunction;

    public static LoggableJson of(@CheckForNull final Object value) {
        return new LoggableJson(value, JsonUtils::write);
    }

    @Override
    public String toString() {
        return toJsonFunction.apply(value);
    }
}
