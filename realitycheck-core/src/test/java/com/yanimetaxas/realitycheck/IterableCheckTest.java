package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThatIterable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class IterableCheckTest {

    /** A lazy iterable that doesn't implement Collection. */
    static Iterable<Integer> rangeIterable(int start, int endExclusive) {
        return () -> new Iterator<>() {
            int current = start;

            @Override
            public boolean hasNext() {
                return current < endExclusive;
            }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                return current++;
            }
        };
    }

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThatIterable(rangeIterable(0, 0)).isEmpty());
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThatIterable(rangeIterable(0, 5)).isNotEmpty());
    }

    @Test
    void hasSize_passes() {
        assertDoesNotThrow(() -> checkThatIterable(rangeIterable(0, 10)).hasSize(10));
    }

    @Test
    void contains_passes() {
        assertDoesNotThrow(() -> checkThatIterable(rangeIterable(0, 10)).contains(5));
    }

    @Test
    void contains_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatIterable(rangeIterable(0, 5)).contains(10));
    }

    @Test
    void doesNotContain_passes() {
        assertDoesNotThrow(() -> checkThatIterable(rangeIterable(0, 5)).doesNotContain(10));
    }

    @Test
    void containsAll_passes() {
        assertDoesNotThrow(() -> checkThatIterable(rangeIterable(0, 10)).containsAll(1, 3, 7));
    }

    @Test
    void allMatch_passes() {
        assertDoesNotThrow(() ->
                checkThatIterable(rangeIterable(0, 10)).allMatch(n -> n >= 0, "non-negative"));
    }

    @Test
    void anyMatch_passes() {
        assertDoesNotThrow(() ->
                checkThatIterable(rangeIterable(0, 10)).anyMatch(n -> n == 5, "equals 5"));
    }

    @Test
    void noneMatch_passes() {
        assertDoesNotThrow(() ->
                checkThatIterable(rangeIterable(0, 10)).noneMatch(n -> n < 0, "negative"));
    }

    @Test
    void worksWithCollectionsToo() {
        assertDoesNotThrow(() ->
                checkThatIterable(List.of("a", "b", "c")).hasSize(3).contains("b"));
    }

    @Test
    void asCollection_bridges() {
        assertDoesNotThrow(() ->
                checkThatIterable(rangeIterable(0, 3)).asCollection().containsExactly(0, 1, 2));
    }

    @Test
    void containsAnyOf_pass_and_fail() {
        assertDoesNotThrow(() -> checkThatIterable(rangeIterable(0, 5)).containsAnyOf(10, 2, 20));
        assertThrows(AssertionError.class, () -> checkThatIterable(rangeIterable(0, 2)).containsAnyOf(5, 6, 7));
    }

    @Test
    void containsNoneOf_pass_and_fail() {
        assertDoesNotThrow(() -> checkThatIterable(rangeIterable(0, 3)).containsNoneOf(10, 11));
        assertThrows(AssertionError.class, () -> checkThatIterable(rangeIterable(0, 5)).containsNoneOf(1, 99));
    }
}
