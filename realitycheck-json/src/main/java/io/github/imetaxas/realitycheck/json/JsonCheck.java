package io.github.imetaxas.realitycheck.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.imetaxas.realitycheck.AbstractCheck;
import io.github.imetaxas.realitycheck.FailureHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Fluent assertions for JSON strings with structural comparison, path queries, and diff output.
 *
 * <pre>{@code
 * import static io.github.imetaxas.realitycheck.json.JsonReality.*;
 *
 * checkThatJson(response).isValidJson().hasField("user.name").fieldEquals("user.name", "Alice");
 * checkThatJson(actual).isStructurallyEqualTo(expected);
 * }</pre>
 */
public final class JsonCheck extends AbstractCheck<JsonCheck, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonNode parsedNode;

    JsonCheck(String actual, FailureHandler handler) {
        super(actual, handler);
    }

    public JsonCheck isValidJson() {
        parse();
        return self();
    }

    public JsonCheck hasField(String dotPath) {
        JsonNode node = navigatePath(dotPath);
        if (node == null || node.isMissingNode()) {
            failureHandler().fail("expected JSON to have field <%s> but it was not found",
                    dotPath);
        }
        return self();
    }

    public JsonCheck doesNotHaveField(String dotPath) {
        JsonNode node = navigatePath(dotPath);
        if (node != null && !node.isMissingNode()) {
            failureHandler().fail("expected JSON not to have field <%s> but found: <%s>",
                    dotPath, node);
        }
        return self();
    }

    public JsonCheck fieldEquals(String dotPath, String expectedValue) {
        JsonNode node = navigatePath(dotPath);
        if (node == null || node.isMissingNode()) {
            failureHandler().fail("expected field <%s> = <%s> but field was not found",
                    dotPath, expectedValue);
        } else {
            String actual = node.isTextual() ? node.asText() : node.toString();
            if (!actual.equals(expectedValue)) {
                failureHandler().fail("expected field <%s> = <%s> but was: <%s>",
                        dotPath, expectedValue, actual);
            }
        }
        return self();
    }

    public JsonCheck fieldEquals(String dotPath, int expectedValue) {
        JsonNode node = navigatePath(dotPath);
        if (node == null || node.isMissingNode()) {
            failureHandler().fail("expected field <%s> = <%d> but field was not found",
                    dotPath, expectedValue);
        } else if (!node.isNumber() || node.asInt() != expectedValue) {
            failureHandler().fail("expected field <%s> = <%d> but was: <%s>",
                    dotPath, expectedValue, node);
        }
        return self();
    }

    public JsonCheck fieldEquals(String dotPath, boolean expectedValue) {
        JsonNode node = navigatePath(dotPath);
        if (node == null || node.isMissingNode()) {
            failureHandler().fail("expected field <%s> = <%s> but field was not found",
                    dotPath, expectedValue);
        } else if (!node.isBoolean() || node.asBoolean() != expectedValue) {
            failureHandler().fail("expected field <%s> = <%s> but was: <%s>",
                    dotPath, expectedValue, node);
        }
        return self();
    }

    public JsonCheck fieldIsNull(String dotPath) {
        JsonNode node = navigatePath(dotPath);
        if (node == null || node.isMissingNode()) {
            failureHandler().fail("expected field <%s> to be null but field was not found",
                    dotPath);
        } else if (!node.isNull()) {
            failureHandler().fail("expected field <%s> to be null but was: <%s>",
                    dotPath, node);
        }
        return self();
    }

    public JsonCheck fieldIsArray(String dotPath) {
        JsonNode node = navigatePath(dotPath);
        if (node == null || node.isMissingNode()) {
            failureHandler().fail("expected field <%s> to be an array but field was not found",
                    dotPath);
        } else if (!node.isArray()) {
            failureHandler().fail("expected field <%s> to be an array but was: <%s>",
                    dotPath, node.getNodeType());
        }
        return self();
    }

    public JsonCheck arrayAtPathHasSize(String dotPath, int expectedSize) {
        JsonNode node = navigatePath(dotPath);
        if (node == null || !node.isArray()) {
            failureHandler().fail("expected an array at <%s>",
                    dotPath);
        } else if (node.size() != expectedSize) {
            failureHandler().fail("expected array at <%s> to have size <%d> but was: <%d>",
                    dotPath, expectedSize, node.size());
        }
        return self();
    }

    /**
     * Structurally compares two JSON strings, ignoring key ordering.
     * On failure, produces a path-level diff showing exactly what differs.
     */
    public JsonCheck isStructurallyEqualTo(String expectedJson) {
        JsonNode actualNode = parse();
        JsonNode expectedNode = parseJson(expectedJson, "expected JSON");
        if (!actualNode.equals(expectedNode)) {
            List<String> diffs = structuralDiff("$", expectedNode, actualNode);
            var sb = new StringBuilder("JSON structures differ:\n");
            for (String d : diffs) {
                sb.append("  ").append(d).append('\n');
            }
            failureHandler().fail(sb.toString());
        }
        return self();
    }

    public JsonCheck isNotStructurallyEqualTo(String otherJson) {
        JsonNode actualNode = parse();
        JsonNode otherNode = parseJson(otherJson, "other JSON");
        if (actualNode.equals(otherNode)) {
            failureHandler().fail("expected JSON structures to differ but they are equal");
        }
        return self();
    }

    public JsonCheck hasSameKeysAs(String otherJson) {
        JsonNode actualNode = parse();
        JsonNode otherNode = parseJson(otherJson, "other JSON");
        if (actualNode.isObject() && otherNode.isObject()) {
            TreeSet<String> actualKeys = new TreeSet<>();
            TreeSet<String> otherKeys = new TreeSet<>();
            actualNode.fieldNames().forEachRemaining(actualKeys::add);
            otherNode.fieldNames().forEachRemaining(otherKeys::add);
            if (!actualKeys.equals(otherKeys)) {
                TreeSet<String> missing = new TreeSet<>(otherKeys);
                missing.removeAll(actualKeys);
                TreeSet<String> extra = new TreeSet<>(actualKeys);
                extra.removeAll(otherKeys);
                failureHandler().fail("JSON keys differ. missing: %s, extra: %s",
                        missing, extra);
            }
        } else {
            failureHandler().fail("hasSameKeysAs requires both values to be JSON objects");
        }
        return self();
    }

    // ── Internal ──────────────────────────────────────────────────────────

    private JsonNode parse() {
        if (parsedNode == null) {
            parsedNode = parseJson(actual(), "actual JSON");
        }
        return parsedNode;
    }

    private JsonNode parseJson(String json, String label) {
        JsonNode result = null;
        try {
            result = MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            failureHandler().fail("invalid %s: %s",
                    label, e.getMessage());
        }
        return result != null ? result : MAPPER.nullNode();
    }

    private JsonNode navigatePath(String dotPath) {
        JsonNode node = parse();
        for (String segment : parsePathSegments(dotPath)) {
            if (node == null || node.isMissingNode()) return null;
            if (segment.matches("\\d+") && node.isArray()) {
                node = node.get(Integer.parseInt(segment));
            } else {
                node = node.get(segment);
            }
        }
        return node;
    }

    static List<String> parsePathSegments(String path) {
        if (path.startsWith("$.")) {
            path = path.substring(2);
        } else if (path.equals("$")) {
            return List.of();
        }

        List<String> segments = new ArrayList<>();
        int len = path.length();
        int i = 0;

        while (i < len) {
            if (path.charAt(i) == '[' && i + 1 < len && path.charAt(i + 1) == '"') {
                int closing = path.indexOf("\"]", i + 2);
                if (closing < 0) {
                    segments.add(path.substring(i));
                    break;
                }
                segments.add(path.substring(i + 2, closing));
                i = closing + 2;
                if (i < len && path.charAt(i) == '.') {
                    i++;
                }
            } else {
                int dot = -1;
                int bracket = -1;
                for (int j = i; j < len; j++) {
                    if (path.charAt(j) == '.') { dot = j; break; }
                    if (path.charAt(j) == '[') { bracket = j; break; }
                }
                if (dot >= 0) {
                    segments.add(path.substring(i, dot));
                    i = dot + 1;
                } else if (bracket >= 0) {
                    if (bracket > i) {
                        segments.add(path.substring(i, bracket));
                    }
                    if (bracket + 1 < len && path.charAt(bracket + 1) == '"') {
                        i = bracket;
                    } else {
                        int closing = path.indexOf(']', bracket + 1);
                        if (closing < 0) {
                            segments.add(path.substring(bracket + 1));
                            break;
                        }
                        segments.add(path.substring(bracket + 1, closing));
                        i = closing + 1;
                        if (i < len && path.charAt(i) == '.') {
                            i++;
                        }
                    }
                } else {
                    segments.add(path.substring(i));
                    break;
                }
            }
        }
        return segments;
    }

    private static List<String> structuralDiff(String path, JsonNode expected, JsonNode actual) {
        List<String> diffs = new ArrayList<>();
        if (expected == null && actual == null) return diffs;
        if (expected == null) {
            diffs.add(path + ": unexpected value " + actual);
            return diffs;
        }
        if (actual == null) {
            diffs.add(path + ": missing (expected " + expected + ")");
            return diffs;
        }
        if (expected.getNodeType() != actual.getNodeType()) {
            diffs.add(path + ": type mismatch — expected " + expected.getNodeType() + " but was " + actual.getNodeType());
            return diffs;
        }
        if (expected.isObject()) {
            TreeSet<String> allKeys = new TreeSet<>();
            expected.fieldNames().forEachRemaining(allKeys::add);
            actual.fieldNames().forEachRemaining(allKeys::add);
            for (String key : allKeys) {
                diffs.addAll(structuralDiff(path + "." + key, expected.get(key), actual.get(key)));
            }
        } else if (expected.isArray()) {
            int max = Math.max(expected.size(), actual.size());
            for (int i = 0; i < max; i++) {
                JsonNode e = i < expected.size() ? expected.get(i) : null;
                JsonNode a = i < actual.size() ? actual.get(i) : null;
                diffs.addAll(structuralDiff(path + "[" + i + "]", e, a));
            }
        } else if (!expected.equals(actual)) {
            diffs.add(path + ": expected " + expected + " but was " + actual);
        }
        return diffs;
    }
}
