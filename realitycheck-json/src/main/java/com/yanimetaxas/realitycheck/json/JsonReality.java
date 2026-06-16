package com.yanimetaxas.realitycheck.json;

import com.yanimetaxas.realitycheck.FailureHandler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Entry point for JSON assertions.
 *
 * <pre>{@code
 * import static com.yanimetaxas.realitycheck.json.JsonReality.*;
 *
 * checkThatJson("{\"name\":\"Alice\"}").hasField("name").fieldEquals("name", "Alice");
 * checkThatJsonFile(path).isValidJson().isStructurallyEqualTo(expectedJson);
 * }</pre>
 */
public final class JsonReality {

    private JsonReality() {}

    public static JsonCheck checkThatJson(String json) {
        return new JsonCheck(json, new FailureHandler());
    }

    /** Alias for {@link #checkThatJson(String)} for AssertJ/Truth migration. */
    public static JsonCheck assertThatJson(String json) {
        return checkThatJson(json);
    }

    public static JsonCheck checkThatJsonFile(Path path) {
        try {
            String content = Files.readString(path);
            return new JsonCheck(content, new FailureHandler());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Alias for {@link #checkThatJsonFile(Path)} for AssertJ/Truth migration. */
    public static JsonCheck assertThatJsonFile(Path path) {
        return checkThatJsonFile(path);
    }
}
