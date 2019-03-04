package com.bordozer.commons.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import javax.annotation.CheckForNull;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

class LocalDateTimeConverterTest {

    private static final LocalDateTimeConverter LOCAL_DATE_TIME_CONVERTER = new LocalDateTimeConverter("yyyy-MM-dd HH:mm:ss");

    @Test
    void shouldReturnNullIfNullProvided() {
        // given

        // when
        @CheckForNull final LocalDateTime actual = LOCAL_DATE_TIME_CONVERTER.convert(null);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnNullIfEmptyProvided() {
        // given

        // when
        @CheckForNull final LocalDateTime actual = LOCAL_DATE_TIME_CONVERTER.convert(StringUtils.EMPTY);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnValue() {
        // given

        // when
        @CheckForNull final LocalDateTime actual = LOCAL_DATE_TIME_CONVERTER.convert("2017-02-24 22:12:56");

        // then
        assertThat(actual).isEqualTo(LocalDateTime.of(2017, 2, 24, 22, 12, 56));
    }

    @Test
    void shouldThrowexceptionIfWrongDate() {
        assertThrows(DateTimeParseException.class, () -> LOCAL_DATE_TIME_CONVERTER.convert("2017/02/24 345667"));
    }
}