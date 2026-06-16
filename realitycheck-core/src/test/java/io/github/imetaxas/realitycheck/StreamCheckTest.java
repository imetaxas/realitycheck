package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThatStream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class StreamCheckTest {

    @Test
    void hasSize_passes() {
        assertDoesNotThrow(() -> checkThatStream(Stream.of("a", "b", "c")).hasSize(3));
    }

    @Test
    void hasSize_fails() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of("a")).hasSize(5));
    }

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThatStream(Stream.empty()).isEmpty());
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThatStream(Stream.of(1)).isNotEmpty());
    }

    @Test
    void contains_passes() {
        assertDoesNotThrow(() -> checkThatStream(Stream.of("x", "y", "z")).contains("y"));
    }

    @Test
    void contains_fails() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of("x")).contains("missing"));
    }

    @Test
    void doesNotContain_passes() {
        assertDoesNotThrow(() -> checkThatStream(Stream.of("a", "b")).doesNotContain("z"));
    }

    @Test
    void containsExactly_passes() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of(1, 2, 3)).containsExactly(1, 2, 3));
    }

    @Test
    void containsExactlyInAnyOrder_passes() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of(3, 1, 2)).containsExactlyInAnyOrder(1, 2, 3));
    }

    @Test
    void allMatch_passes() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of(2, 4, 6)).allMatch(n -> n % 2 == 0, "even"));
    }

    @Test
    void allMatch_fails() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of(2, 3, 4)).allMatch(n -> n % 2 == 0, "even"));
    }

    @Test
    void anyMatch_passes() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of(1, 2, 3)).anyMatch(n -> n > 2, "greater than 2"));
    }

    @Test
    void noneMatch_passes() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of(1, 2, 3)).noneMatch(n -> n > 10, "greater than 10"));
    }

    @Test
    void noneMatch_fails() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of(1, 20, 3)).noneMatch(n -> n > 10, "greater than 10"));
    }

    @Test
    void first_extractor() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of("hello", "world"))
                        .first().isEqualTo("hello"));
    }

    @Test
    void toList_extractor() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of("a", "b")).toList().hasSize(2));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of("a", "b", "c"))
                        .hasSize(3)
                        .contains("b")
                        .doesNotContain("z"));
    }

    @Test
    void noneMatch_pass() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of(1, 2, 3)).noneMatch(x -> x > 10, "greater than 10"));
    }

    @Test
    void noneMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of(1, 2, 3)).noneMatch(x -> x == 2, "equals 2"));
    }

    @Test
    void first_pass() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of("a", "b")).first().isEqualTo("a"));
    }

    @Test
    void first_fail_empty() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.<String>empty()).first());
    }

    @Test
    void hasSize_pass() {
        assertDoesNotThrow(() -> checkThatStream(Stream.of(1, 2, 3)).hasSize(3));
    }

    @Test
    void hasSize_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of(1, 2)).hasSize(5));
    }

    @Test
    void allMatch_pass() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of(2, 4, 6)).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void allMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of(2, 3, 6)).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void anyMatch_pass() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of(1, 2, 3)).anyMatch(x -> x == 2, "equals 2"));
    }

    @Test
    void anyMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of(1, 2, 3)).anyMatch(x -> x > 10, "greater than 10"));
    }

    @Test
    void containsExactly_pass() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of("a", "b")).containsExactly("a", "b"));
    }

    @Test
    void containsExactly_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of("a", "b")).containsExactly("b", "a"));
    }

    @Test
    void containsExactlyInAnyOrder_pass() {
        assertDoesNotThrow(() ->
                checkThatStream(Stream.of("b", "a")).containsExactlyInAnyOrder("a", "b"));
    }

    @Test
    void containsExactlyInAnyOrder_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatStream(Stream.of("a", "b")).containsExactlyInAnyOrder("x", "y"));
    }

    @Test
    void containsExactly_failsOnWrongOrder() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.of("a", "b")).containsExactly("b", "a"));
    }

    @Test
    void containsExactlyInAnyOrder_failsOnWrongElements() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.of(1, 2)).containsExactlyInAnyOrder(3, 4));
    }

    @Test
    void toList_extractorChains() {
        assertDoesNotThrow(
                () -> checkThatStream(Stream.of("a", "b")).toList().hasSize(2));
    }

    @Test
    void toList_failsBridgedAssertion() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.of("a")).toList().contains("z"));
    }

    @Test
    void isEmpty_failsOnNonEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.of(1)).isEmpty());
    }

    @Test
    void isNotEmpty_failsOnEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.empty()).isNotEmpty());
    }

    @Test
    void noneMatch_failsWhenSomeMatch() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.of(1, 20, 3)).noneMatch(n -> n > 10, "gt 10"));
    }

    @Test
    void first_failsOnEmptyStream() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.empty()).first());
    }

    @Test
    void doesNotContain_failsWhenPresent() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.of("x", "y")).doesNotContain("x"));
    }

    @Test
    void containsExactlyInAnyOrder_failsWhenDuplicateCountsDiffer() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.of(1, 1, 2)).containsExactlyInAnyOrder(1, 2, 2));
    }

    @Test
    void containsExactlyInAnyOrder_passesWithMatchingDuplicates() {
        assertDoesNotThrow(
                () -> checkThatStream(Stream.of(1, 1, 2)).containsExactlyInAnyOrder(2, 1, 1));
    }

    @Test
    void anyMatch_failsWhenNoneMatch() {
        assertThrows(AssertionError.class,
                () -> checkThatStream(Stream.of(1, 2, 3)).anyMatch(n -> n > 100, "gt 100"));
    }
}
