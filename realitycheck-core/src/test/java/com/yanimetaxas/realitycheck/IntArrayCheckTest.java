package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class IntArrayCheckTest {

    @Test
    void hasLength_passes() {
        assertDoesNotThrow(() -> checkThat(new int[]{1, 2, 3}).hasLength(3));
    }

    @Test
    void hasLength_fails() {
        assertThrows(AssertionError.class, () -> checkThat(new int[]{1}).hasLength(5));
    }

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(new int[]{}).isEmpty());
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(new int[]{42}).isNotEmpty());
    }

    @Test
    void contains_passes() {
        assertDoesNotThrow(() -> checkThat(new int[]{10, 20, 30}).contains(20));
    }

    @Test
    void contains_fails() {
        assertThrows(AssertionError.class, () -> checkThat(new int[]{1, 2}).contains(99));
    }

    @Test
    void doesNotContain_passes() {
        assertDoesNotThrow(() -> checkThat(new int[]{1, 2}).doesNotContain(99));
    }

    @Test
    void containsExactly_passes() {
        assertDoesNotThrow(() -> checkThat(new int[]{1, 2, 3}).containsExactly(1, 2, 3));
    }

    @Test
    void containsExactly_fails() {
        assertThrows(AssertionError.class, () -> checkThat(new int[]{1, 2}).containsExactly(2, 1));
    }

    @Test
    void containsExactlyInAnyOrder_passes() {
        assertDoesNotThrow(() ->
                checkThat(new int[]{3, 1, 2}).containsExactlyInAnyOrder(1, 2, 3));
    }

    @Test
    void isSorted_passes() {
        assertDoesNotThrow(() -> checkThat(new int[]{1, 2, 3, 4}).isSorted());
    }

    @Test
    void isSorted_fails() {
        assertThrows(AssertionError.class, () -> checkThat(new int[]{3, 1, 2}).isSorted());
    }

    @Test
    void allMatch_passes() {
        assertDoesNotThrow(() ->
                checkThat(new int[]{2, 4, 6}).allMatch(n -> n % 2 == 0, "even"));
    }

    @Test
    void allMatch_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(new int[]{2, 3, 6}).allMatch(n -> n % 2 == 0, "even"));
    }

    @Test
    void anyMatch_passes() {
        assertDoesNotThrow(() ->
                checkThat(new int[]{1, 2, 3}).anyMatch(n -> n > 2, "greater than 2"));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat(new int[]{1, 2, 3}).hasLength(3).contains(2).isSorted());
    }

    @Test
    void allMatch_pass() {
        assertDoesNotThrow(() ->
                checkThat(new int[]{2, 4, 6}).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void allMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new int[]{2, 3, 6}).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void containsExactly_pass() {
        assertDoesNotThrow(() ->
                checkThat(new int[]{1, 2, 3}).containsExactly(1, 2, 3));
    }

    @Test
    void containsExactly_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new int[]{1, 2, 3}).containsExactly(1, 2, 4));
    }

    @Test
    void isSorted_pass() {
        assertDoesNotThrow(() -> checkThat(new int[]{1, 2, 3}).isSorted());
    }

    @Test
    void isSorted_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new int[]{3, 1, 2}).isSorted());
    }

    @Test
    void anyMatch_pass() {
        assertDoesNotThrow(() ->
                checkThat(new int[]{1, 2, 3}).anyMatch(x -> x == 2, "equals 2"));
    }

    @Test
    void anyMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new int[]{1, 2, 3}).anyMatch(x -> x > 10, "greater than 10"));
    }

    @Test
    void containsExactlyInAnyOrder_pass() {
        assertDoesNotThrow(() ->
                checkThat(new int[]{3, 1, 2}).containsExactlyInAnyOrder(1, 2, 3));
    }

    @Test
    void containsExactlyInAnyOrder_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new int[]{1, 2, 3}).containsExactlyInAnyOrder(4, 5, 6));
    }

    @Test
    void isEmpty_failsWhenNotEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{1, 2}).isEmpty());
    }

    @Test
    void isNotEmpty_failsWhenEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{}).isNotEmpty());
    }

    @Test
    void hasLength_failsOnWrongLength() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{1}).hasLength(5));
    }

    @Test
    void contains_failsWhenMissing() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{1, 2}).contains(99));
    }

    @Test
    void doesNotContain_failsWhenPresent() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{1, 2, 3}).doesNotContain(2));
    }

    @Test
    void containsExactly_failsOnMismatch() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{1, 2}).containsExactly(2, 1));
    }

    @Test
    void containsExactlyInAnyOrder_failsOnWrongElements() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{1, 2}).containsExactlyInAnyOrder(3, 4));
    }

    @Test
    void isSorted_failsWhenUnsorted() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{3, 1}).isSorted());
    }

    @Test
    void allMatch_failsWhenOneFails() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{2, 3, 4}).allMatch(n -> n % 2 == 0, "even"));
    }

    @Test
    void anyMatch_failsWhenNoneMatch() {
        assertThrows(AssertionError.class,
                () -> checkThat(new int[]{1, 2, 3}).anyMatch(n -> n > 100, "gt 100"));
    }
}
