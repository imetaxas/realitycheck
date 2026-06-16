package com.yanimetaxas.realitycheck.json;

import static com.yanimetaxas.realitycheck.json.JsonReality.*;
import static org.junit.jupiter.api.Assertions.*;

import com.yanimetaxas.realitycheck.FailureHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JsonCheckTest {

    static final String SAMPLE = """
            {
              "name": "Alice",
              "age": 30,
              "active": true,
              "address": {
                "city": "Stockholm",
                "zip": "11122"
              },
              "tags": ["java", "testing"]
            }
            """;

    @Test
    void isValidJson_passes() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).isValidJson());
    }

    @Test
    void isValidJson_failsForInvalid() {
        assertThrows(AssertionError.class, () -> checkThatJson("{bad json").isValidJson());
    }

    @Test
    void hasField_topLevel() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).hasField("name"));
    }

    @Test
    void hasField_nested() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).hasField("address.city"));
    }

    @Test
    void hasField_failsForMissing() {
        var e = assertThrows(AssertionError.class, () -> checkThatJson(SAMPLE).hasField("email"));
        assertTrue(e.getMessage().contains("email"));
    }

    @Test
    void doesNotHaveField_passes() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).doesNotHaveField("email"));
    }

    @Test
    void fieldEquals_string() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).fieldEquals("name", "Alice"));
    }

    @Test
    void fieldEquals_nested() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).fieldEquals("address.city", "Stockholm"));
    }

    @Test
    void fieldEquals_int() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).fieldEquals("age", 30));
    }

    @Test
    void fieldEquals_boolean() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).fieldEquals("active", true));
    }

    @Test
    void fieldEquals_failsWithActualValue() {
        var e = assertThrows(AssertionError.class, () -> checkThatJson(SAMPLE).fieldEquals("name", "Bob"));
        assertTrue(e.getMessage().contains("Alice"));
        assertTrue(e.getMessage().contains("Bob"));
    }

    @Test
    void fieldIsArray_passes() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).fieldIsArray("tags"));
    }

    @Test
    void arrayAtPathHasSize_passes() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).arrayAtPathHasSize("tags", 2));
    }

    @Test
    void isStructurallyEqualTo_sameContent() {
        String other = """
                {"age":30,"name":"Alice","active":true,"address":{"zip":"11122","city":"Stockholm"},"tags":["java","testing"]}
                """;
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).isStructurallyEqualTo(other));
    }

    @Test
    void isStructurallyEqualTo_failsWithPathDiff() {
        String different = """
                {"name":"Bob","age":25,"active":false,"address":{"city":"Gothenburg","zip":"41111"},"tags":["python"]}
                """;
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).isStructurallyEqualTo(different));
        assertTrue(e.getMessage().contains("$.name"));
        assertTrue(e.getMessage().contains("$.age"));
    }

    @Test
    void hasSameKeysAs_passes() {
        String sameKeys = """
                {"name":"X","age":0,"active":false,"address":{},"tags":[]}
                """;
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).hasSameKeysAs(sameKeys));
    }

    @Test
    void hasSameKeysAs_failsWithMissingAndExtra() {
        String differentKeys = """
                {"name":"X","email":"x@y.com"}
                """;
        var e = assertThrows(AssertionError.class, () -> checkThatJson(SAMPLE).hasSameKeysAs(differentKeys));
        assertTrue(e.getMessage().contains("missing") || e.getMessage().contains("extra"));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE)
                .isValidJson()
                .hasField("name")
                .fieldEquals("name", "Alice")
                .hasField("address.city")
                .fieldIsArray("tags"));
    }

    @Test
    void arrayIndex_navigation() {
        String json = """
                {"items":[{"id":1},{"id":2}]}
                """;
        assertDoesNotThrow(() -> checkThatJson(json).fieldEquals("items.0.id", 1));
        assertDoesNotThrow(() -> checkThatJson(json).fieldEquals("items.1.id", 2));
    }

    @Test
    void fieldEquals_boolean_failsWhenFieldIsString() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"name\":\"Alice\"}").fieldEquals("name", true));
    }

    @Test
    void fieldEquals_boolean_failsWhenFieldIsNumber() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"n\":42}").fieldEquals("n", false));
    }

    @Test
    void navigatePath_digitKeyOnObject() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"0\":\"zero\",\"1\":\"one\"}").fieldEquals("0", "zero"));
    }

    @Test
    void navigatePath_digitKeyOnObjectMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").hasField("0"));
    }

    @Test
    void navigatePath_objectArrayObjectValue() {
        String json = """
                {"data":{"items":[{"id":1,"label":"first"},{"id":2,"label":"second"}]}}
                """;
        assertDoesNotThrow(() -> checkThatJson(json).fieldEquals("data.items.1.label", "second"));
    }

    @Test
    void navigatePath_objectArrayObjectValue_missing() {
        String json = """
                {"data":{"items":[{"id":1}]}}
                """;
        assertThrows(AssertionError.class,
                () -> checkThatJson(json).hasField("data.items.0.missing"));
    }

    @Test
    void navigatePath_objectArrayObject_deepChain() {
        String json = """
                {"a":{"b":[{"c":{"d":"deep"}}]}}
                """;
        assertDoesNotThrow(() -> checkThatJson(json).fieldEquals("a.b.0.c.d", "deep"));
    }

    @Test
    void isStructurallyEqualTo_arrayElementTypeMismatch_numberVsString() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("[1]").isStructurallyEqualTo("[\"1\"]"));
        assertTrue(e.getMessage().contains("type mismatch"));
    }

    @Test
    void isStructurallyEqualTo_topLevelTypeMismatch_objectVsArray() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").isStructurallyEqualTo("[1]"));
        assertTrue(e.getMessage().contains("type mismatch"));
    }

    @Test
    void isStructurallyEqualTo_nestedArraysDifferentLengths() {
        String actual = "{\"m\":[[1,2],[3]]}";
        String expected = "{\"m\":[[1,2],[3,4]]}";
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(actual).isStructurallyEqualTo(expected));
        assertTrue(e.getMessage().contains("missing"));
    }

    @Test
    void isStructurallyEqualTo_deepNestedValueDiff() {
        String actual = "{\"a\":{\"b\":{\"c\":\"x\"}}}";
        String expected = "{\"a\":{\"b\":{\"c\":\"y\"}}}";
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(actual).isStructurallyEqualTo(expected));
        assertTrue(e.getMessage().contains("$.a.b.c"));
    }

    @Test
    void hasSameKeysAs_actualHasExtraKeysOnly() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1,\"b\":2,\"c\":3}").hasSameKeysAs("{\"a\":1}"));
        assertTrue(e.getMessage().contains("extra"));
    }

    @Test
    void hasSameKeysAs_actualMissingKeysOnly() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").hasSameKeysAs("{\"a\":1,\"b\":2,\"c\":3}"));
        assertTrue(e.getMessage().contains("missing"));
    }

    @Test
    void hasSameKeysAs_bothArrays_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("[1]").hasSameKeysAs("{\"a\":1}"));
    }

    @Test
    void isNotStructurallyEqualTo_failsForIdenticalNested() {
        String json = "{\"a\":{\"b\":[1,2,3]}}";
        assertThrows(AssertionError.class,
                () -> checkThatJson(json).isNotStructurallyEqualTo(json));
    }

    @Test
    void arrayAtPathHasSize_failsWhenPathMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"x\":1}").arrayAtPathHasSize("missing", 0));
    }

    @Test
    void arrayAtPathHasSize_failShowsBothSizes() {
        String json = "{\"arr\":[1,2,3]}";
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(json).arrayAtPathHasSize("arr", 10));
        assertTrue(e.getMessage().contains("10"));
        assertTrue(e.getMessage().contains("3"));
    }

    @Test
    void doesNotHaveField_failsWhenNestedFieldExists() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":{\"b\":1}}").doesNotHaveField("a.b"));
    }

    @Test
    void parseCaching_multipleChainsReuse() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"x\":1,\"y\":\"two\",\"z\":true}")
                        .isValidJson()
                        .hasField("x")
                        .hasField("y")
                        .fieldEquals("x", 1)
                        .fieldEquals("y", "two")
                        .fieldEquals("z", true));
    }

    @Test
    void structuralDiff_actualHasMoreNestedArrayElements() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":[1,2,3]}").isStructurallyEqualTo("{\"a\":[1,2]}"));
        assertTrue(e.getMessage().contains("unexpected"));
    }

    @Test
    void structuralDiff_expectedHasMoreNestedArrayElements() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":[1]}").isStructurallyEqualTo("{\"a\":[1,2,3]}"));
        assertTrue(e.getMessage().contains("missing"));
    }

    @Test
    void fieldEquals_string_arrayNodeUsesToString() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"arr\":[1,2]}").fieldEquals("arr", "[1,2]"));
    }

    @Test
    void fieldIsNull_passOnNestedNull() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"a\":{\"b\":null}}").fieldIsNull("a.b"));
    }

    @Test
    void isNotStructurallyEqualTo_invalidOtherJson() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").isNotStructurallyEqualTo("{bad"));
    }

    @Test
    void fieldIsArray_pass() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).fieldIsArray("tags"));
    }

    @Test
    void fieldIsArray_failWhenNotArray() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldIsArray("name"));
        assertTrue(e.getMessage().contains("array"));
    }

    @Test
    void fieldIsArray_failWhenFieldMissing() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldIsArray("nonexistent"));
        assertTrue(e.getMessage().contains("not found"));
    }

    @Test
    void fieldIsNull_pass() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"nullField\":null,\"name\":\"Alice\"}").fieldIsNull("nullField"));
    }

    @Test
    void fieldIsNull_failWhenNotNull() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldIsNull("name"));
        assertTrue(e.getMessage().contains("null"));
    }

    @Test
    void fieldIsNull_failWhenMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldIsNull("missing"));
    }

    @Test
    void isStructurallyEqualTo_typeMismatch() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"x\":1}").isStructurallyEqualTo("{\"x\":\"1\"}"));
        assertTrue(e.getMessage().contains("type mismatch"));
    }

    @Test
    void isStructurallyEqualTo_missingExpectedKey() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").isStructurallyEqualTo("{\"a\":1,\"b\":2}"));
        assertTrue(e.getMessage().contains("missing"));
    }

    @Test
    void isStructurallyEqualTo_unexpectedKey() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1,\"b\":2}").isStructurallyEqualTo("{\"a\":1}"));
        assertTrue(e.getMessage().contains("unexpected"));
    }

    @Test
    void isStructurallyEqualTo_arrayLengthDifference() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("[1,2,3]").isStructurallyEqualTo("[1,2]"));
        assertTrue(e.getMessage().contains("unexpected") || e.getMessage().contains("[2]"));
    }

    @Test
    void isStructurallyEqualTo_valueDifference() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"v\":\"abc\"}").isStructurallyEqualTo("{\"v\":\"xyz\"}"));
        assertTrue(e.getMessage().contains("expected"));
    }

    @Test
    void isValidJson_invalidInput() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("not json at all").isValidJson());
        assertTrue(e.getMessage().contains("invalid"));
    }

    @Test
    void hasField_missingNestedField() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).hasField("address.country"));
    }

    @Test
    void hasField_deepMissingPath() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).hasField("a.b.c.d"));
    }

    @Test
    void fieldEquals_string_wrongValue() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldEquals("name", "Bob"));
        assertTrue(e.getMessage().contains("Bob"));
        assertTrue(e.getMessage().contains("Alice"));
    }

    @Test
    void fieldEquals_int_missingField() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldEquals("missing", 42));
    }

    @Test
    void fieldEquals_boolean_missingField() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldEquals("missing", true));
    }

    @Test
    void fieldEquals_boolean_wrongValue() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldEquals("active", false));
    }

    @Test
    void fieldEquals_int_wrongValue() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldEquals("age", 99));
    }

    @Test
    void fieldEquals_nonTextualNodeUsesToString() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"obj\":{\"a\":1}}").fieldEquals("obj", "{\"a\":1}"));
    }

    @Test
    void doesNotHaveField_passWhenAbsent() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).doesNotHaveField("email"));
    }

    @Test
    void doesNotHaveField_failWhenPresent() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).doesNotHaveField("name"));
    }

    @Test
    void arrayAtPathHasSize_pass() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).arrayAtPathHasSize("tags", 2));
    }

    @Test
    void arrayAtPathHasSize_wrongSize() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).arrayAtPathHasSize("tags", 5));
        assertTrue(e.getMessage().contains("5"));
    }

    @Test
    void arrayAtPathHasSize_notAnArray() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).arrayAtPathHasSize("name", 1));
    }

    @Test
    void isNotStructurallyEqualTo_pass() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"a\":1}").isNotStructurallyEqualTo("{\"a\":2}"));
    }

    @Test
    void isNotStructurallyEqualTo_failWhenEqual() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").isNotStructurallyEqualTo("{\"a\":1}"));
    }

    @Test
    void hasSameKeysAs_pass() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"a\":1,\"b\":2}").hasSameKeysAs("{\"a\":99,\"b\":88}"));
    }

    @Test
    void hasSameKeysAs_failWithDifferentKeys() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").hasSameKeysAs("{\"b\":2}"));
        assertTrue(e.getMessage().contains("missing") || e.getMessage().contains("extra"));
    }

    @Test
    void hasSameKeysAs_failWhenNotBothObjects() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("[1,2]").hasSameKeysAs("[3,4]"));
    }

    @Test
    void navigatePath_arrayIndex() {
        assertDoesNotThrow(() ->
                checkThatJson(SAMPLE).fieldEquals("tags.0", "java"));
    }

    @Test
    void navigatePath_nestedArrayIndex() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"nested\":{\"list\":[1,2,3]}}").fieldEquals("nested.list.1", "2"));
    }

    @Test
    void navigatePath_outOfBoundsIndex() {
        assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).hasField("tags.99"));
    }

    @Test
    void checkThatJsonFile_pass(@TempDir Path dir) throws IOException {
        Path f = dir.resolve("test.json");
        Files.writeString(f, "{\"key\":\"value\"}");
        assertDoesNotThrow(() ->
                checkThatJsonFile(f).isValidJson().hasField("key").fieldEquals("key", "value"));
    }

    @Test
    void checkThatJsonFile_missingFile(@TempDir Path dir) {
        assertThrows(java.io.UncheckedIOException.class,
                () -> checkThatJsonFile(dir.resolve("no-such-file.json")));
    }

    @Test
    void structuralDiff_nestedObjectDifference() {
        String actual = "{\"outer\":{\"inner\":\"a\"}}";
        String expected = "{\"outer\":{\"inner\":\"b\"}}";
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(actual).isStructurallyEqualTo(expected));
        assertTrue(e.getMessage().contains("$.outer.inner"));
    }

    @Test
    void structuralDiff_arrayElementDifference() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("[1,2,3]").isStructurallyEqualTo("[1,2,4]"));
        assertTrue(e.getMessage().contains("$[2]"));
    }

    @Test
    void structuralDiff_expectedHasMoreArrayElements() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("[1]").isStructurallyEqualTo("[1,2]"));
        assertTrue(e.getMessage().contains("missing"));
    }

    @Test
    void fieldEquals_string_missingField() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson(SAMPLE).fieldEquals("missing", "value"));
        assertTrue(e.getMessage().contains("not found"));
    }

    @Test
    void fieldEquals_int_wrongType_notNumber() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"name\":\"Alice\"}").fieldEquals("name", 42));
    }

    @Test
    void fieldEquals_boolean_wrongBoolValue() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"flag\":true}").fieldEquals("flag", false));
        assertTrue(e.getMessage().contains("false") || e.getMessage().contains("true"));
    }

    @Test
    void fieldIsNull_fieldMissingFails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").fieldIsNull("nonexistent"));
        assertTrue(e.getMessage().contains("not found"));
    }

    @Test
    void fieldIsArray_notFoundFails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").fieldIsArray("missing"));
        assertTrue(e.getMessage().contains("not found"));
    }

    @Test
    void arrayAtPathHasSize_notArrayFails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatJson("{\"x\":\"hello\"}").arrayAtPathHasSize("x", 1));
        assertTrue(e.getMessage().contains("array"));
    }

    @Test
    void hasSameKeysAs_failsWhenActualIsArray() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("[1,2]").hasSameKeysAs("{\"a\":1}"));
    }

    @Test
    void hasSameKeysAs_failsWhenOtherIsArray() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").hasSameKeysAs("[1,2]"));
    }

    @Test
    void isStructurallyEqualTo_invalidExpectedJson() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"a\":1}").isStructurallyEqualTo("{bad"));
    }

    @Test
    void doesNotHaveField_passForDeepMissing() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"a\":{\"b\":1}}").doesNotHaveField("a.c"));
    }

    @Test
    void fieldEquals_string_nonTextualReturnsToString() {
        assertDoesNotThrow(() ->
                checkThatJson("{\"n\":42}").fieldEquals("n", "42"));
    }

    // Soft-mode tests: exercise branches only reachable when fail() doesn't throw

    static class CollectingHandler extends FailureHandler {
        final List<String> messages = new ArrayList<>();

        @Override
        public void fail(String format, Object... args) {
            messages.add(args.length == 0 ? format : String.format(format, args));
        }
    }

    @Test
    void softMode_hasField_continuesAfterInvalidJson() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.hasField("x");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_doesNotHaveField_continuesAfterInvalidJson() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.doesNotHaveField("x");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldEquals_string_continuesAfterInvalidJson() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.fieldEquals("x", "val");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldEquals_int_continuesAfterInvalidJson() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.fieldEquals("x", 42);
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldEquals_boolean_continuesAfterInvalidJson() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.fieldEquals("x", true);
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldIsNull_continuesAfterInvalidJson() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.fieldIsNull("x");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldIsArray_continuesAfterInvalidJson() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.fieldIsArray("x");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_arrayAtPathHasSize_continuesAfterInvalidJson() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.arrayAtPathHasSize("x", 1);
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_isStructurallyEqualTo_continuesAfterInvalidJson() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.isStructurallyEqualTo("{\"a\":1}");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_parseJson_returnsNullNodeForInvalidInput() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{bad", handler);
        check.isValidJson();
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_doesNotHaveField_fieldExists() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"a\":1}", handler);
        check.doesNotHaveField("a");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldEquals_string_wrongValue() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"name\":\"Alice\"}", handler);
        check.fieldEquals("name", "Bob");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldIsNull_notNull() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"x\":\"val\"}", handler);
        check.fieldIsNull("x");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_isNotStructurallyEqualTo_equal() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"a\":1}", handler);
        check.isNotStructurallyEqualTo("{\"a\":1}");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_hasSameKeysAs_keysDiffer() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"a\":1}", handler);
        check.hasSameKeysAs("{\"b\":2}");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_hasSameKeysAs_notBothObjects() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("[1,2]", handler);
        check.hasSameKeysAs("[3,4]");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldEquals_int_wrongValue() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"n\":42}", handler);
        check.fieldEquals("n", 99);
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldEquals_boolean_wrongValue() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"flag\":true}", handler);
        check.fieldEquals("flag", false);
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_fieldIsArray_notArray() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"x\":\"str\"}", handler);
        check.fieldIsArray("x");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_arrayAtPathHasSize_wrongSize() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"a\":[1,2]}", handler);
        check.arrayAtPathHasSize("a", 5);
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void softMode_isStructurallyEqualTo_different() {
        var handler = new CollectingHandler();
        var check = new JsonCheck("{\"a\":1}", handler);
        check.isStructurallyEqualTo("{\"a\":2}");
        assertFalse(handler.messages.isEmpty());
    }

    @Test
    void assertThatJson_alias_works() {
        assertDoesNotThrow(() -> JsonReality.assertThatJson("{\"a\":1}").isValidJson());
    }

    @Test
    void assertThatJsonFile_alias_works(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("test.json");
        Files.writeString(file, "{\"a\":1}");
        assertDoesNotThrow(() -> JsonReality.assertThatJsonFile(file).isValidJson());
    }

    // ── Dot-path enhancement tests ───────────────────────────────────────

    @Test
    void navigatePath_dollarPrefixStripped() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).fieldEquals("$.name", "Alice"));
    }

    @Test
    void navigatePath_dollarPrefixNested() {
        assertDoesNotThrow(() -> checkThatJson(SAMPLE).fieldEquals("$.address.city", "Stockholm"));
    }

    @Test
    void navigatePath_bracketNotationForKeyWithDot() {
        String json = """
                {"app.version": "1.2.3", "name": "myapp"}
                """;
        assertDoesNotThrow(() -> checkThatJson(json).fieldEquals("[\"app.version\"]", "1.2.3"));
    }

    @Test
    void navigatePath_mixedBracketAndDotNotation() {
        String json = """
                {"user": {"app.version": {"name": "beta"}}}
                """;
        assertDoesNotThrow(() ->
                checkThatJson(json).fieldEquals("user[\"app.version\"].name", "beta"));
    }

    @Test
    void navigatePath_dollarPrefixWithBracketNotation() {
        String json = """
                {"config": {"db.host": "localhost"}}
                """;
        assertDoesNotThrow(() ->
                checkThatJson(json).fieldEquals("$.config[\"db.host\"]", "localhost"));
    }

    @Test
    void fieldEquals_objectDiffersFromExpectedString() {
        assertThrows(AssertionError.class,
                () -> checkThatJson("{\"obj\":{\"nested\":1}}").fieldEquals("obj", "x"));
    }

    @Test
    void navigatePath_arrayIndexFieldAccess() {
        String json = """
                {"users": [{"name": "Alice"}, {"name": "Bob"}]}
                """;
        assertDoesNotThrow(() ->
                checkThatJson(json).fieldEquals("users[0].name", "Alice"));
        assertDoesNotThrow(() ->
                checkThatJson(json).fieldEquals("users[1].name", "Bob"));
    }

    @Test
    void navigatePath_dollarPrefixArrayIndex() {
        String json = """
                {"items": ["a", "b", "c"]}
                """;
        assertDoesNotThrow(() ->
                checkThatJson(json).fieldEquals("$.items[1]", "b"));
    }

    @Test
    void navigatePath_nestedArrayIndices() {
        String json = """
                {"matrix": [[1, 2], [3, 4]]}
                """;
        assertDoesNotThrow(() ->
                checkThatJson(json).fieldEquals("matrix[0][1]", "2"));
    }

    @Test
    void navigatePath_unclosedArrayBracket() {
        String json = """
                {"items": ["a", "b"]}
                """;
        assertDoesNotThrow(() ->
                checkThatJson(json).fieldEquals("items[0", "a"));
    }
}
