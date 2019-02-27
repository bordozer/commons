package com.bordozer.commons.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    private static String readResource(final Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (final IOException var2) {
            throw new IllegalArgumentException(String.format("Path '%s' does not exists", path));
        }
    }
}
