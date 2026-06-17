package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MapCheckTest {

    @Nested
    class WhenActualIsNull {

        @Test
        void allGuardedAssertions_shortCircuit_whenActualIsNull() {
            var handler = new SoftFailureHandler();
            var check = new MapCheck<String, String>(null, handler);
            check.isEmpty().isNotEmpty().hasSize(0).containsKey("k").doesNotContainKey("k");
            assertTrue(handler.failures().size() >= 5, "each null-guarded method should record a failure");
        }
    }

    @Nested
    class HasSameEntriesAs {

        @Test
        void hasSameEntriesAs_extraKey_fails() {
            Map<String, String> actual = Map.of("a", "1", "extra", "x");
            Map<String, String> expected = Map.of("a", "1");
            var e = assertThrows(AssertionError.class,
                    () -> checkThat(actual).hasSameEntriesAs(expected));
            assertTrue(e.getMessage().contains("extra:"));
        }

        @Test
        void hasSameEntriesAs_missingKey_fails() {
            Map<String, String> actual = Map.of("a", "1");
            Map<String, String> expected = Map.of("a", "1", "missing", "y");
            var e = assertThrows(AssertionError.class,
                    () -> checkThat(actual).hasSameEntriesAs(expected));
            assertTrue(e.getMessage().contains("missing:"));
        }

        @Test
        void hasSameEntriesAs_changedValue_fails() {
            Map<String, String> actual = Map.of("a", "new");
            Map<String, String> expected = Map.of("a", "old");
            var e = assertThrows(AssertionError.class,
                    () -> checkThat(actual).hasSameEntriesAs(expected));
            assertTrue(e.getMessage().contains("changed:"));
        }
    }

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(Map.of()).isEmpty());
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(Map.of("a", 1)).isNotEmpty());
    }

    @Test
    void hasSize_passes() {
        assertDoesNotThrow(() -> checkThat(Map.of("a", 1, "b", 2)).hasSize(2));
    }

    @Test
    void containsKey_passes() {
        assertDoesNotThrow(() -> checkThat(Map.of("name", "Alice")).containsKey("name"));
    }

    @Test
    void containsKey_failsWithActualKeys() {
        var e = assertThrows(AssertionError.class,
                () -> checkThat(Map.of("name", "Alice")).containsKey("age"));
        assertTrue(e.getMessage().contains("name"));
    }

    @Test
    void containsEntry_passes() {
        assertDoesNotThrow(() -> checkThat(Map.of("a", 1)).containsEntry("a", 1));
    }

    @Test
    void containsEntry_failsOnWrongValue() {
        var e = assertThrows(AssertionError.class,
                () -> checkThat(Map.of("a", 1)).containsEntry("a", 2));
        assertTrue(e.getMessage().contains("2"));
        assertTrue(e.getMessage().contains("1"));
    }

    @Test
    void containsValue_passes() {
        assertDoesNotThrow(() -> checkThat(Map.of("a", 1)).containsValue(1));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat(Map.of("x", 10)).isNotNull().isNotEmpty().hasSize(1).containsKey("x"));
    }

    @Test
    void hasSameEntriesAs_pass() {
        Map<String, Integer> map = Map.of("a", 1, "b", 2);
        assertDoesNotThrow(() -> checkThat(map).hasSameEntriesAs(Map.of("a", 1, "b", 2)));
    }

    @Test
    void hasSameEntriesAs_fail_changedValues() {
        Map<String, Integer> actual = new HashMap<>(Map.of("a", 1, "b", 2, "c", 3));
        Map<String, Integer> expected = Map.of("a", 1, "b", 99, "c", 3);
        var e = assertThrows(AssertionError.class, () ->
                checkThat(actual).hasSameEntriesAs(expected));
        assertTrue(e.getMessage().contains("changed"));
    }

    @Test
    void hasSameEntriesAs_fail_extraKeys() {
        Map<String, Integer> actual = new HashMap<>(Map.of("a", 1, "b", 2, "extra", 99));
        Map<String, Integer> expected = Map.of("a", 1, "b", 2);
        var e = assertThrows(AssertionError.class, () ->
                checkThat(actual).hasSameEntriesAs(expected));
        assertTrue(e.getMessage().contains("extra"));
    }

    @Test
    void hasSameEntriesAs_fail_missingKeys() {
        Map<String, Integer> actual = new HashMap<>(Map.of("a", 1));
        Map<String, Integer> expected = Map.of("a", 1, "b", 2);
        var e = assertThrows(AssertionError.class, () ->
                checkThat(actual).hasSameEntriesAs(expected));
        assertTrue(e.getMessage().contains("missing"));
    }

    @Test
    void hasSameEntriesAs_fail_allThreeKinds() {
        Map<String, Integer> actual = new HashMap<>();
        actual.put("a", 999);
        actual.put("extra", 42);

        Map<String, Integer> expected = Map.of("a", 1, "missing", 7);
        var e = assertThrows(AssertionError.class, () ->
                checkThat(actual).hasSameEntriesAs(expected));
        String msg = e.getMessage();
        assertTrue(msg.contains("changed") && msg.contains("extra") && msg.contains("missing"));
    }

    @Test
    void containsEntry_pass() {
        assertDoesNotThrow(() -> checkThat(Map.of("a", 1)).containsEntry("a", 1));
    }

    @Test
    void containsEntry_fail_wrongValue() {
        assertThrows(AssertionError.class, () ->
                checkThat(Map.of("a", 1)).containsEntry("a", 2));
    }

    @Test
    void containsEntry_fail_missingKey() {
        assertThrows(AssertionError.class, () ->
                checkThat(Map.of("a", 1)).containsEntry("b", 1));
    }

    @Test
    void doesNotContainKey_passesAndFails() {
        assertDoesNotThrow(() -> checkThat(Map.of("a", 1)).doesNotContainKey("missing"));
        assertThrows(AssertionError.class, () -> checkThat(Map.of("a", 1)).doesNotContainKey("a"));
    }

    @Test
    void containsAllKeys_passesAndFails() {
        var m = Map.of("a", 1, "b", 2, "c", 3);
        assertDoesNotThrow(() -> checkThat(m).containsAllKeys(Set.of("a", "b")));
        assertThrows(AssertionError.class, () -> checkThat(m).containsAllKeys(Set.of("a", "z")));
    }

    @Test
    void atPath_nestedNavigation() {
        Map<String, Object> inner = Map.of("host", "localhost", "port", 5432);
        Map<String, Object> root = Map.of("db", inner);
        assertDoesNotThrow(() -> checkThat(root).atPath("db.host").isEqualTo("localhost"));
    }

    @Test
    void atPath_missingKey() {
        Map<String, Object> root = Map.of("a", Map.of("b", 1));
        assertThrows(AssertionError.class, () -> checkThat(root).atPath("a.c").isEqualTo("x"));
    }

    @Test
    void atPath_nonMapAtPath() {
        Map<String, Object> root = Map.of("a", "leaf");
        assertThrows(AssertionError.class, () -> checkThat(root).atPath("a.b").isEqualTo("x"));
    }

    @Test
    void stringAtPath_passes() {
        Map<String, Object> root = Map.of("msg", "hello");
        assertDoesNotThrow(() -> checkThat(root).stringAtPath("msg").isEqualTo("hello"));
    }

    @Test
    void stringAtPath_nullValue_fails() {
        var m = new HashMap<String, Object>();
        m.put("n", null);
        assertThrows(AssertionError.class, () -> checkThat(m).stringAtPath("n").isNotEmpty());
    }

    @Test
    void mapAtPath_passes() {
        Map<String, Object> inner = Map.of("k", "v");
        Map<String, Object> root = Map.of("section", inner);
        assertDoesNotThrow(() -> checkThat(root).mapAtPath("section").containsEntry("k", "v"));
    }

    @Test
    void mapAtPath_notMap_fails() {
        Map<String, Object> root = Map.of("section", "text");
        assertThrows(AssertionError.class, () -> checkThat(root).mapAtPath("section").isEmpty());
    }
}
