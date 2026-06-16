package com.yanimetaxas.realitycheck;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fluent assertions for {@link Map} values.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public final class MapCheck<K, V> extends AbstractCheck<MapCheck<K, V>, Map<K, V>> {

    MapCheck(Map<K, V> actual, FailureHandler handler) {
        super(actual, handler);
    }

    public MapCheck<K, V> isEmpty() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().isEmpty(),
                "expected an empty map but had <%d> entries", actual().size());
    }

    public MapCheck<K, V> isNotEmpty() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), !actual().isEmpty(),
                "expected a non-empty map");
    }

    public MapCheck<K, V> hasSize(int expected) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().size() == expected,
                "expected map size <%d> but was: <%d>", expected, actual().size());
    }

    public MapCheck<K, V> containsKey(K key) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().containsKey(key),
                "expected map to contain key <%s> but keys were: %s", key, actual().keySet());
    }

    public MapCheck<K, V> doesNotContainKey(K key) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), !actual().containsKey(key),
                "expected map not to contain key <%s>", key);
    }

    public MapCheck<K, V> containsValue(V value) {
        return failureHandler().check(self(), actual().containsValue(value),
                "expected map to contain value <%s> but values were: %s", value, actual().values());
    }

    public MapCheck<K, V> containsEntry(K key, V value) {
        if (!actual().containsKey(key)) {
            failureHandler().fail("expected map to contain entry <%s=%s> but key was absent",
                    key, value);
        } else if (!Objects.equals(actual().get(key), value)) {
            failureHandler().fail("expected map entry <%s=%s> but value was: <%s>",
                    key, value, actual().get(key));
        }
        return self();
    }

    public MapCheck<K, V> containsAllKeys(Set<K> keys) {
        Set<K> missing = keys.stream()
                .filter(k -> !actual().containsKey(k))
                .collect(Collectors.toSet());
        return failureHandler().check(self(), missing.isEmpty(),
                "expected map to contain keys %s but missing: %s", keys, missing);
    }

    /**
     * Navigates into a nested map structure using a dot-delimited path.
     * Each segment is used as a key to traverse nested {@code Map} values.
     *
     * <pre>{@code
     * checkThat(config).atPath("database.connection.host").isEqualTo("localhost");
     * }</pre>
     *
     * @throws AssertionError if any segment is missing or not a Map
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectCheck<T> atPath(String dotPath) {
        Object current = actual();
        var traversed = new StringBuilder();
        for (String segment : dotPath.split("\\.")) {
            if (!(current instanceof Map<?, ?> map)) {
                failureHandler().fail("expected a Map at path <%s> but was: <%s> (%s)",
                        traversed, current, current == null ? "null" : current.getClass().getSimpleName());
                return new ObjectCheck<>(null, failureHandler());
            }
            if (!map.containsKey(segment)) {
                failureHandler().fail("path <%s> not found — key <%s> is missing; available keys: %s",
                        dotPath, segment, map.keySet());
                return new ObjectCheck<>(null, failureHandler());
            }
            current = map.get(segment);
            if (!traversed.isEmpty()) traversed.append('.');
            traversed.append(segment);
        }
        return new ObjectCheck<>((T) current, failureHandler());
    }

    /**
     * Navigates into a nested map and returns the value as a {@link StringCheck}.
     */
    public StringCheck stringAtPath(String dotPath) {
        Object value = atPath(dotPath).actual();
        if (value == null) {
            failureHandler().fail("expected a String at path <%s> but was null", dotPath);
        }
        return new StringCheck(value != null ? value.toString() : null, failureHandler());
    }

    /**
     * Navigates into a nested map and returns the value as a {@link MapCheck}
     * for further map assertions.
     */
    @SuppressWarnings("unchecked")
    public <K2, V2> MapCheck<K2, V2> mapAtPath(String dotPath) {
        Object value = atPath(dotPath).actual();
        if (!(value instanceof Map<?, ?>)) {
            failureHandler().fail("expected a Map at path <%s> but was: <%s>", dotPath, value);
            return new MapCheck<>((Map<K2, V2>) Map.of(), failureHandler());
        }
        return new MapCheck<>((Map<K2, V2>) value, failureHandler());
    }

    public MapCheck<K, V> hasSameEntriesAs(Map<K, V> expected) {
        if (!actual().equals(expected)) {
            Set<K> allKeys = new LinkedHashSet<>(actual().keySet());
            allKeys.addAll(expected.keySet());

            var sb = new StringBuilder("maps differ:\n");
            for (K key : allKeys) {
                boolean inActual = actual().containsKey(key);
                boolean inExpected = expected.containsKey(key);
                if (inActual && inExpected) {
                    if (!Objects.equals(actual().get(key), expected.get(key))) {
                        sb.append("  changed: ").append(key)
                                .append(" = ").append(expected.get(key))
                                .append(" -> ").append(actual().get(key)).append('\n');
                    }
                } else if (inActual) {
                    sb.append("  extra:   ").append(key).append(" = ").append(actual().get(key)).append('\n');
                } else {
                    sb.append("  missing: ").append(key).append(" = ").append(expected.get(key)).append('\n');
                }
            }
            failureHandler().fail(sb.toString());
        }
        return self();
    }
}
