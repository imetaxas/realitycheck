package com.yanimetaxas.realitycheck.yaml;

import com.yanimetaxas.realitycheck.FailureHandler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Entry point for YAML assertions.
 */
public final class YamlReality {

    private YamlReality() {}

    public static YamlCheck checkThatYaml(String yaml) {
        return new YamlCheck(yaml, new FailureHandler());
    }

    /** Alias for {@link #checkThatYaml(String)} for AssertJ/Truth migration. */
    public static YamlCheck assertThatYaml(String yaml) {
        return checkThatYaml(yaml);
    }

    public static YamlCheck checkThatYamlFile(Path path) {
        try {
            return new YamlCheck(Files.readString(path), new FailureHandler());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Alias for {@link #checkThatYamlFile(Path)} for AssertJ/Truth migration. */
    public static YamlCheck assertThatYamlFile(Path path) {
        return checkThatYamlFile(path);
    }
}
