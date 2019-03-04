package com.bordozer.commons.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.CheckForNull;

import org.springframework.core.convert.converter.Converter;

public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private final DateTimeFormatter formatter;

    public LocalDateTimeConverter(final String dateTimeFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
    }

    @Override
    public LocalDateTime convert(@CheckForNull final String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        return LocalDateTime.parse(source, formatter);
    }
}
