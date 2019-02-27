package com.bordozer.commons.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoggableJsonTest {

    private static final String EXPECTED_CONVERTER_OBJECT = FileUtils.readSystemResource("tests/LoggableJsonTest/expected-converter-object.json");

    @Test
    void shouldConvertObject() {
        // given
        final TestObject object = new TestObject("A value", 124);

        // when
        final String actual = LoggableJson.of(object).toString();

        // then
        assertThat(actual).isEqualTo(EXPECTED_CONVERTER_OBJECT);
    }

    @Test
    void shouldConvertNull() {
        // given

        // when
        final String actual = LoggableJson.of(null).toString();

        // then
        assertThat(actual).isEqualTo("null");
    }

    @Test
    void shouldConvertPrimitiveType() {
        // given

        // when
        final String actual = LoggableJson.of(456L).toString();

        // then
        assertThat(actual).isEqualTo("456");
    }

    @Getter
    @RequiredArgsConstructor
    private static class TestObject {
        private final String valueSrt;
        private final Integer valueInt;
    }
}