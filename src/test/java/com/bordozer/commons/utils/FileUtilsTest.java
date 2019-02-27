package com.bordozer.commons.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileUtilsTest {

    @Test
    void shouldRearResourceIfExists() {
        // given

        // when
        final String actual = FileUtils.readSystemResource("tests/FileUtilsTest/resource.txt");

        // then
        assertThat(actual).isEqualTo("Just a string in a text file");
    }

    @Test
    void shouldThrowExceptionIfNullIsProvided() {
        assertThrows(IllegalArgumentException.class, () -> FileUtils.readSystemResource(null));
    }

    @Test
    void shouldThrowExceptionIfResourceDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> FileUtils.readSystemResource("missed resource path"));
    }
}