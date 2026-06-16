package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThatArray;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArrayCheckTest {

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThatArray(new String[0]).isEmpty());
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThatArray("a", "b").isNotEmpty());
    }

    @Test
    void hasLength_passes() {
        assertDoesNotThrow(() -> checkThatArray("a", "b", "c").hasLength(3));
    }

    @Test
    void hasLength_fails() {
        var e = assertThrows(AssertionError.class, () -> checkThatArray("a").hasLength(5));
        assertTrue(e.getMessage().contains("5"));
        assertTrue(e.getMessage().contains("1"));
    }

    @Test
    void contains_passes() {
        assertDoesNotThrow(() -> checkThatArray("x", "y", "z").contains("y"));
    }

    @Test
    void contains_fails() {
        assertThrows(AssertionError.class, () -> checkThatArray("a", "b").contains("z"));
    }

    @Test
    void doesNotContain_passes() {
        assertDoesNotThrow(() -> checkThatArray(1, 2, 3).doesNotContain(5));
    }

    @Test
    void containsExactly_passes() {
        assertDoesNotThrow(() -> checkThatArray("a", "b", "c").containsExactly("a", "b", "c"));
    }

    @Test
    void containsExactly_failsOnWrongOrder() {
        assertThrows(AssertionError.class,
                () -> checkThatArray("a", "b").containsExactly("b", "a"));
    }

    @Test
    void containsExactlyInAnyOrder_passes() {
        assertDoesNotThrow(() ->
                checkThatArray("a", "b", "c").containsExactlyInAnyOrder("c", "a", "b"));
    }

    @Test
    void containsAll_passes() {
        assertDoesNotThrow(() -> checkThatArray(1, 2, 3, 4, 5).containsAll(2, 4));
    }

    @Test
    void allMatch_passes() {
        assertDoesNotThrow(() ->
                checkThatArray(2, 4, 6).allMatch(n -> n % 2 == 0, "is even"));
    }

    @Test
    void allMatch_failsWithOffender() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatArray(2, 3, 4).allMatch(n -> n % 2 == 0, "is even"));
        assertTrue(e.getMessage().contains("3"));
    }

    @Test
    void anyMatch_passes() {
        assertDoesNotThrow(() ->
                checkThatArray(1, 2, 3).anyMatch(n -> n > 2, "greater than 2"));
    }

    @Test
    void isSorted_passes() {
        assertDoesNotThrow(() -> checkThatArray("a", "b", "c").isSorted());
    }

    @Test
    void isSorted_fails() {
        assertThrows(AssertionError.class, () -> checkThatArray("c", "a", "b").isSorted());
    }

    @Test
    void asCollection_bridges() {
        assertDoesNotThrow(() ->
                checkThatArray("a", "b").asCollection().hasSize(2).contains("a"));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThatArray("hello", "world").isNotNull().isNotEmpty().hasLength(2).contains("hello"));
    }

    @Test
    void containsExactly_pass() {
        assertDoesNotThrow(() ->
                checkThatArray("a", "b", "c").containsExactly("a", "b", "c"));
    }

    @Test
    void containsExactly_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatArray("a", "b").containsExactly("a", "c"));
    }

    @Test
    void isSorted_pass() {
        assertDoesNotThrow(() -> checkThatArray(1, 2, 3).isSorted());
    }

    @Test
    void isSorted_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatArray(3, 1, 2).isSorted());
    }

    @Test
    void allMatch_pass() {
        assertDoesNotThrow(() ->
                checkThatArray(2, 4, 6).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void allMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatArray(2, 3, 6).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void anyMatch_pass() {
        assertDoesNotThrow(() ->
                checkThatArray(1, 2, 3).anyMatch(x -> x == 2, "equals 2"));
    }

    @Test
    void anyMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatArray(1, 2, 3).anyMatch(x -> x > 10, "greater than 10"));
    }

    @Test
    void noneMatch_pass() {
        assertDoesNotThrow(() ->
                checkThatArray("a", "b").containsExactlyInAnyOrder("b", "a"));
    }

    @Test
    void containsExactlyInAnyOrder_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatArray("a", "b").containsExactlyInAnyOrder("x", "y"));
    }

    @Test
    void containsAll_pass() {
        assertDoesNotThrow(() -> checkThatArray("a", "b", "c").containsAll("a", "c"));
    }

    @Test
    void containsAll_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatArray("a", "b").containsAll("a", "z"));
    }

    @Test
    void asCollection_extractor() {
        assertDoesNotThrow(() -> checkThatArray(1, 2).asCollection().hasSize(2));
    }

    @Test
    void isEmpty_failsOnNonEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThatArray("a", "b").isEmpty());
    }

    @Test
    void isNotEmpty_failsOnEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThatArray(new String[0]).isNotEmpty());
    }

    @Test
    void hasLength_failsOnWrongSize() {
        assertThrows(AssertionError.class,
                () -> checkThatArray("a", "b").hasLength(5));
    }

    @Test
    void contains_failsWhenMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatArray("a", "b").contains("z"));
    }

    @Test
    void doesNotContain_failsWhenPresent() {
        assertThrows(AssertionError.class,
                () -> checkThatArray("a", "b").doesNotContain("a"));
    }

    @Test
    void containsAll_failsWhenMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatArray("a", "b").containsAll("a", "z"));
    }

    @Test
    void containsExactly_failsOnMismatch() {
        assertThrows(AssertionError.class,
                () -> checkThatArray("a", "b").containsExactly("a", "c"));
    }

    @Test
    void containsExactlyInAnyOrder_failsOnWrongElements() {
        assertThrows(AssertionError.class,
                () -> checkThatArray("a", "b").containsExactlyInAnyOrder("x", "y"));
    }

    @Test
    void isSorted_failsWhenUnsorted() {
        assertThrows(AssertionError.class,
                () -> checkThatArray(3, 1, 2).isSorted());
    }

    @Test
    void allMatch_failsWhenOneFails() {
        assertThrows(AssertionError.class,
                () -> checkThatArray(1, 2, 3).allMatch(n -> n > 2, "gt 2"));
    }

    @Test
    void anyMatch_failsWhenNoneMatch() {
        assertThrows(AssertionError.class,
                () -> checkThatArray(1, 2, 3).anyMatch(n -> n > 10, "gt 10"));
    }

    @Test
    void containsExactlyInAnyOrder_failsWhenDuplicateCountsDiffer() {
        assertThrows(AssertionError.class,
                () -> checkThatArray(1, 1, 2).containsExactlyInAnyOrder(1, 2, 2));
    }

    @Test
    void containsExactlyInAnyOrder_passesWithMatchingDuplicates() {
        assertDoesNotThrow(
                () -> checkThatArray(1, 1, 2).containsExactlyInAnyOrder(2, 1, 1));
    }

    @Test
    void asCollection_bridgesCorrectly() {
        assertDoesNotThrow(
                () -> checkThatArray("a", "b").asCollection().hasSize(2));
    }

    @Test
    void asCollection_failsBridgedAssertion() {
        assertThrows(AssertionError.class,
                () -> checkThatArray("a", "b").asCollection().contains("z"));
    }

    @Test
    void isSorted_failsWhenElementsNotComparable() {
        assertThrows(AssertionError.class,
                () -> checkThatArray(new Object(), new Object()).isSorted());
    }
}
