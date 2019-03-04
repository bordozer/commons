package com.bordozer.commons.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.annotation.CheckForNull;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

class LocalDateConverterTest {

    private static final LocalDateConverter LOCAL_DATE_CONVERTER = new LocalDateConverter("yyyy-MM-dd");

    @Test
    void shouldReturnNullIfNullProvided() {
        // given

        // when
        @CheckForNull final LocalDate actual = LOCAL_DATE_CONVERTER.convert(null);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnNullIfEmptyProvided() {
        // given

        // when
        @CheckForNull final LocalDate actual = LOCAL_DATE_CONVERTER.convert(StringUtils.EMPTY);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void shouldReturnValue() {
        // given

        // when
        @CheckForNull final LocalDate actual = LOCAL_DATE_CONVERTER.convert("2017-02-24");

        // then
        assertThat(actual).isEqualTo(LocalDate.of(2017, 2, 24));
    }

    @Test
    void shouldThrowexceptionIfWrongDate() {
        assertThrows(DateTimeParseException.class, () -> LOCAL_DATE_CONVERTER.convert("2017/02/24"));
    }
}