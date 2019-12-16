package com.bordozer.commons.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import lombok.SneakyThrows;

public final class FileUtils {

    private FileUtils() {
    }

    public static String readSystemResource(final String location) {
        if (location == null) {
            throw new IllegalArgumentException("Location is null");
        }
        try {
            return readResource(Paths.get(ClassLoader.getSystemResource(location).toURI()));
        } catch (final URISyntaxException | RuntimeException ex) {
            throw new IllegalArgumentException(String.format("Location '%s' does not exists", location));
        }
    }

    @SneakyThrows
    public static File getResourceFile(final String name) {
        final URL res = Objects.requireNonNull(FileUtils.class.getClassLoader().getResource(name));
        return Paths.get(res.toURI()).toFile();
    }


    private static String readResource(final Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (final IOException var2) {
            throw new IllegalArgumentException(String.format("Path '%s' does not exists", path));
        }
    }
}
