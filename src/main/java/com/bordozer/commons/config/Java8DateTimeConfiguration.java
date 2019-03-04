package com.bordozer.commons.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SuppressWarnings("PMD.AccessorMethodGeneration")
@Configuration
public class Java8DateTimeConfiguration {

    @Value("${application.properties.dateFormat}")
    private String dateFormat;

    @Value("${application.properties.dateTimeFormat}")
    private String dateTimeFormat;

    @Bean
    @Primary
    public ObjectMapper serializingObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        return objectMapper;
    }

    @Bean
    public LocalDateConverter localDateConverter() {
        return new LocalDateConverter(dateFormat);
    }

    @Bean
    public LocalDateTimeConverter localDateTimeConverter() {
        return new LocalDateTimeConverter(dateTimeFormat);
    }

    public class LocalDateSerializer extends JsonSerializer<LocalDate> {

        @Override
        public void serialize(final LocalDate value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(dateFormat)));
        }
    }

    public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
            return LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(dateFormat));
        }
    }

    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(final LocalDateTime value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(dateTimeFormat)));
        }
    }

    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(dateTimeFormat));
        }
    }
}
