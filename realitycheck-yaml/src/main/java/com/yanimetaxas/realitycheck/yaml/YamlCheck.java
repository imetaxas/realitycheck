package com.yanimetaxas.realitycheck.yaml;

import com.yanimetaxas.realitycheck.AbstractCheck;
import com.yanimetaxas.realitycheck.FailureHandler;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Fluent assertions for YAML strings with dot-path queries.
 *
 * <pre>{@code
 * import static com.yanimetaxas.realitycheck.yaml.YamlReality.*;
 *
 * checkThatYaml(config).isValidYaml().pathEquals("server.port", 8080);
 * }</pre>
 */
public final class YamlCheck extends AbstractCheck<YamlCheck, String> {

    private Object parsed;

    YamlCheck(String actual, FailureHandler handler) {
        super(actual, handler);
    }

    public YamlCheck isValidYaml() {
        parse();
        return self();
    }

    public YamlCheck hasPath(String dotPath) {
        Object value = navigatePath(dotPath);
        if (value == null) {
            failureHandler().fail("expected YAML to have path <%s> but it was not found",
                    dotPath);
        }
        return self();
    }

    public YamlCheck pathEquals(String dotPath, Object expectedValue) {
        Object value = navigatePath(dotPath);
        if (value == null) {
            failureHandler().fail("expected path <%s> = <%s> but path was not found",
                    dotPath, expectedValue);
        } else if (!Objects.equals(value, expectedValue)) {
            failureHandler().fail("expected path <%s> = <%s> but was: <%s>",
                    dotPath, expectedValue, value);
        }
        return self();
    }

    public YamlCheck pathIsNull(String dotPath) {
        parse();
        Object value = navigatePath(dotPath);
        if (value != null) {
            failureHandler().fail("expected path <%s> to be null but was: <%s>",
                    dotPath, value);
        }
        return self();
    }

    public YamlCheck hasKey(String key) {
        Object root = parse();
        if (root instanceof Map<?, ?> map) {
            if (!map.containsKey(key)) {
                failureHandler().fail("expected YAML root to contain key <%s> but keys were: %s",
                        key, map.keySet());
            }
        } else {
            failureHandler().fail("expected YAML root to be a map but was: %s",
                    root == null ? "null" : root.getClass().getSimpleName());
        }
        return self();
    }

    public YamlCheck pathIsList(String dotPath) {
        Object value = navigatePath(dotPath);
        if (!(value instanceof List)) {
            failureHandler().fail("expected path <%s> to be a list but was: <%s>",
                    dotPath, value == null ? "null" : value.getClass().getSimpleName());
        }
        return self();
    }

    public YamlCheck pathListHasSize(String dotPath, int expectedSize) {
        Object value = navigatePath(dotPath);
        if (value instanceof List<?> list) {
            if (list.size() != expectedSize) {
                failureHandler().fail("expected list at <%s> to have size <%d> but was: <%d>",
                        dotPath, expectedSize, list.size());
            }
        } else {
            failureHandler().fail("expected path <%s> to be a list",
                    dotPath);
        }
        return self();
    }

    // ── Internal ──────────────────────────────────────────────────────────

    private static final int MAX_YAML_LENGTH = 10 * 1024 * 1024; // 10 MB

    private Object parse() {
        if (parsed == null) {
            if (actual().length() > MAX_YAML_LENGTH) {
                failureHandler().fail("YAML input exceeds maximum allowed size (%d bytes > %d bytes)",
                        actual().length(), MAX_YAML_LENGTH);
                return null;
            }
            try {
                parsed = new Yaml().load(actual());
            } catch (Exception e) {
                failureHandler().fail("invalid YAML: %s",
                    e.getMessage());
            }
        }
        return parsed;
    }

    private Object navigatePath(String dotPath) {
        Object current = parse();
        for (String segment : dotPath.split("\\.")) {
            if (current instanceof Map<?, ?> map) {
                current = map.get(segment);
            } else if (current instanceof List<?> list) {
                try {
                    current = list.get(Integer.parseInt(segment));
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    return null;
                }
            } else {
                return null;
            }
        }
        return current;
    }
}
