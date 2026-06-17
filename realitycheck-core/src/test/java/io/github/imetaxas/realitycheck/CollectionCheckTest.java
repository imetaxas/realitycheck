package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CollectionCheckTest {

    @Nested
    class WhenActualIsNull {

        @Test
        void guardedAssertions_shortCircuit_whenActualIsNull() {
            var handler = new SoftFailureHandler();
            var check = new CollectionCheck<String>((Collection<String>) null, handler);
            check.isEmpty().isNotEmpty().hasSize(0);
            assertTrue(handler.failures().size() >= 3);
        }
    }

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(List.of()).isEmpty());
    }

    @Test
    void isEmpty_fails() {
        var e = assertThrows(AssertionError.class, () -> checkThat(List.of(1, 2)).isEmpty());
        assertTrue(e.getMessage().contains("<2> elements"), "message was: " + e.getMessage());
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(List.of("a")).isNotEmpty());
    }

    @Test
    void hasSize_passes() {
        assertDoesNotThrow(() -> checkThat(List.of(1, 2, 3)).hasSize(3));
    }

    @Test
    void contains_passes() {
        assertDoesNotThrow(() -> checkThat(List.of("a", "b", "c")).contains("b"));
    }

    @Test
    void contains_failsWithActualCollection() {
        var e = assertThrows(AssertionError.class, () -> checkThat(List.of("a", "b")).contains("z"));
        assertTrue(e.getMessage().contains("z"));
    }

    @Test
    void doesNotContain_passes() {
        assertDoesNotThrow(() -> checkThat(List.of(1, 2)).doesNotContain(3));
    }

    @Test
    void containsAll_passes() {
        assertDoesNotThrow(() -> checkThat(List.of(1, 2, 3)).containsAll(1, 3));
    }

    @Test
    void containsAll_failsWithMissing() {
        var e = assertThrows(AssertionError.class, () -> checkThat(List.of(1, 2)).containsAll(1, 5));
        assertTrue(e.getMessage().contains("5"));
    }

    @Test
    void containsExactly_passes() {
        assertDoesNotThrow(() -> checkThat(List.of("a", "b")).containsExactly("a", "b"));
    }

    @Test
    void containsExactly_failsOnWrongOrder() {
        assertThrows(AssertionError.class, () -> checkThat(List.of("a", "b")).containsExactly("b", "a"));
    }

    @Test
    void containsExactlyInAnyOrder_passes() {
        assertDoesNotThrow(() -> checkThat(List.of("a", "b")).containsExactlyInAnyOrder("b", "a"));
    }

    @Test
    void containsAnyOf_passes() {
        assertDoesNotThrow(() -> checkThat(List.of(1, 2, 3)).containsAnyOf(5, 3));
    }

    @Test
    void containsNoneOf_passes() {
        assertDoesNotThrow(() -> checkThat(List.of(1, 2)).containsNoneOf(3, 4));
    }

    @Test
    void allMatch_passes() {
        assertDoesNotThrow(() -> checkThat(List.of(2, 4, 6)).allMatch(n -> n % 2 == 0, "is even"));
    }

    @Test
    void allMatch_failsWithOffendingElement() {
        var e = assertThrows(AssertionError.class,
                () -> checkThat(List.of(2, 3, 4)).allMatch(n -> n % 2 == 0, "is even"));
        assertTrue(e.getMessage().contains("3"));
    }

    @Test
    void anyMatch_passes() {
        assertDoesNotThrow(() -> checkThat(List.of(1, 2, 3)).anyMatch(n -> n > 2, "greater than 2"));
    }

    @Test
    void noneMatch_passes() {
        assertDoesNotThrow(() -> checkThat(List.of(1, 2, 3)).noneMatch(n -> n > 5, "greater than 5"));
    }

    @Test
    void first_extracts() {
        assertDoesNotThrow(() -> checkThat(List.of("hello", "world")).first().isEqualTo("hello"));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat(List.of(1, 2, 3)).isNotEmpty().hasSize(3).contains(2).doesNotContain(5));
    }

    @Test
    void containsExactlyInAnyOrder_pass() {
        assertDoesNotThrow(() ->
                checkThat(List.of(3, 1, 2)).containsExactlyInAnyOrder(1, 2, 3));
    }

    @Test
    void containsExactlyInAnyOrder_fail_differentElements() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2, 3)).containsExactlyInAnyOrder(4, 5, 6));
    }

    @Test
    void containsExactlyInAnyOrder_fail_differentSize() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2)).containsExactlyInAnyOrder(1, 2, 3));
    }

    @Test
    void containsExactly_pass() {
        assertDoesNotThrow(() ->
                checkThat(List.of(1, 2, 3)).containsExactly(1, 2, 3));
    }

    @Test
    void containsExactly_fail_wrongOrder() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2, 3)).containsExactly(3, 2, 1));
    }

    @Test
    void containsExactly_fail_extra() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2, 3)).containsExactly(1, 2));
    }

    @Test
    void containsExactly_fail_missing() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2)).containsExactly(1, 2, 3));
    }

    @Test
    void anyMatch_pass() {
        assertDoesNotThrow(() ->
                checkThat(List.of(1, 2, 3)).anyMatch(x -> x > 2, "greater than 2"));
    }

    @Test
    void anyMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2, 3)).anyMatch(x -> x > 10, "greater than 10"));
    }

    @Test
    void noneMatch_pass() {
        assertDoesNotThrow(() ->
                checkThat(List.of(1, 2, 3)).noneMatch(x -> x > 10, "greater than 10"));
    }

    @Test
    void noneMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2, 3)).noneMatch(x -> x == 2, "equals 2"));
    }

    @Test
    void first_pass() {
        assertDoesNotThrow(() -> checkThat(List.of("a", "b")).first().isEqualTo("a"));
    }

    @Test
    void first_fail_empty() {
        assertThrows(AssertionError.class, () ->
                checkThat(Collections.<String>emptyList()).first());
    }

    @Test
    void containsAnyOf_pass() {
        assertDoesNotThrow(() ->
                checkThat(List.of(1, 2, 3)).containsAnyOf(5, 3, 9));
    }

    @Test
    void containsAnyOf_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2, 3)).containsAnyOf(7, 8, 9));
    }

    @Test
    void containsNoneOf_pass() {
        assertDoesNotThrow(() ->
                checkThat(List.of(1, 2, 3)).containsNoneOf(7, 8, 9));
    }

    @Test
    void containsNoneOf_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2, 3)).containsNoneOf(3, 4, 5));
    }

    @Test
    void allMatch_pass() {
        assertDoesNotThrow(() ->
                checkThat(List.of(2, 4, 6)).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void allMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(2, 3, 6)).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void hasSizeGreaterThan_pass() {
        assertDoesNotThrow(() -> checkThat(List.of(1, 2, 3)).hasSizeGreaterThan(2));
    }

    @Test
    void hasSizeGreaterThan_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2, 3)).hasSizeGreaterThan(3));
    }

    @Test
    void hasSizeLessThan_pass() {
        assertDoesNotThrow(() -> checkThat(List.of(1)).hasSizeLessThan(5));
    }

    @Test
    void hasSizeLessThan_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(List.of(1, 2, 3)).hasSizeLessThan(3));
    }

    @Test
    void containsExactlyInAnyOrder_failsWhenDuplicateCountsDiffer() {
        assertThrows(AssertionError.class,
                () -> checkThat(List.of(1, 1, 2)).containsExactlyInAnyOrder(1, 2, 2));
    }

    @Test
    void containsExactlyInAnyOrder_passesWithMatchingDuplicates() {
        assertDoesNotThrow(
                () -> checkThat(List.of(1, 1, 2)).containsExactlyInAnyOrder(2, 1, 1));
    }

    @Test
    void doesNotContain_fails() {
        assertThrows(AssertionError.class,
                () -> checkThat(List.of(1, 2, 3)).doesNotContain(2));
    }
}
