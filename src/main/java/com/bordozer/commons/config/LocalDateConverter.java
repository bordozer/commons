package com.bordozer.commons.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.annotation.CheckForNull;

import org.springframework.core.convert.converter.Converter;

public class LocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter;

    public LocalDateConverter(final String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public LocalDate convert(@CheckForNull final String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        return LocalDate.parse(source, formatter);
    }
}
