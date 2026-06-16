package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DoubleArrayCheckTest {

    @Test
    void hasLength_passes() {
        assertDoesNotThrow(() -> checkThat(new double[]{1.0, 2.5}).hasLength(2));
    }

    @Test
    void contains_passes() {
        assertDoesNotThrow(() -> checkThat(new double[]{1.0, 2.5, 3.7}).contains(2.5));
    }

    @Test
    void containsCloseTo_passes() {
        assertDoesNotThrow(() ->
                checkThat(new double[]{3.141, 2.718}).containsCloseTo(3.14, 0.01));
    }

    @Test
    void containsCloseTo_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(new double[]{1.0, 2.0}).containsCloseTo(5.0, 0.01));
    }

    @Test
    void containsExactly_passes() {
        assertDoesNotThrow(() ->
                checkThat(new double[]{1.0, 2.0}).containsExactly(1.0, 2.0));
    }

    @Test
    void containsExactlyInAnyOrder_passes() {
        assertDoesNotThrow(() ->
                checkThat(new double[]{3.0, 1.0, 2.0}).containsExactlyInAnyOrder(1.0, 2.0, 3.0));
    }

    @Test
    void isSorted_passes() {
        assertDoesNotThrow(() -> checkThat(new double[]{1.1, 2.2, 3.3}).isSorted());
    }

    @Test
    void isSorted_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(new double[]{3.0, 1.0}).isSorted());
    }

    @Test
    void allMatch_passes() {
        assertDoesNotThrow(() ->
                checkThat(new double[]{1.5, 2.5, 3.5}).allMatch(d -> d > 0, "positive"));
    }

    @Test
    void isEmpty_and_isNotEmpty() {
        assertDoesNotThrow(() -> checkThat(new double[]{}).isEmpty());
        assertDoesNotThrow(() -> checkThat(new double[]{1.0}).isNotEmpty());
    }

    @Test
    void containsCloseTo_pass() {
        assertDoesNotThrow(() ->
                checkThat(new double[]{1.0, 3.14, 5.0}).containsCloseTo(3.15, 0.02));
    }

    @Test
    void containsCloseTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new double[]{1.0, 2.0, 3.0}).containsCloseTo(9.99, 0.001));
    }

    @Test
    void allMatch_pass() {
        assertDoesNotThrow(() ->
                checkThat(new double[]{1.0, 2.0, 3.0}).allMatch(x -> x > 0, "positive"));
    }

    @Test
    void allMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new double[]{1.0, -2.0, 3.0}).allMatch(x -> x > 0, "positive"));
    }

    @Test
    void isSorted_pass() {
        assertDoesNotThrow(() -> checkThat(new double[]{1.0, 2.0, 3.0}).isSorted());
    }

    @Test
    void isSorted_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new double[]{3.0, 1.0, 2.0}).isSorted());
    }

    @Test
    void containsExactly_pass() {
        assertDoesNotThrow(() ->
                checkThat(new double[]{1.0, 2.0}).containsExactly(1.0, 2.0));
    }

    @Test
    void containsExactly_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new double[]{1.0, 2.0}).containsExactly(1.0, 3.0));
    }

    @Test
    void containsExactlyInAnyOrder_pass() {
        assertDoesNotThrow(() ->
                checkThat(new double[]{2.0, 1.0}).containsExactlyInAnyOrder(1.0, 2.0));
    }

    @Test
    void containsExactlyInAnyOrder_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new double[]{1.0, 2.0}).containsExactlyInAnyOrder(3.0, 4.0));
    }

    @Test
    void contains_pass() {
        assertDoesNotThrow(() -> checkThat(new double[]{1.0, 2.0}).contains(2.0));
    }

    @Test
    void contains_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new double[]{1.0, 2.0}).contains(9.0));
    }

    @Test
    void doesNotContain_pass() {
        assertDoesNotThrow(() -> checkThat(new double[]{1.0, 2.0}).doesNotContain(9.0));
    }

    @Test
    void doesNotContain_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new double[]{1.0, 2.0}).doesNotContain(2.0));
    }

    @Test
    void isEmpty_failsWhenNotEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{1.0}).isEmpty());
    }

    @Test
    void isNotEmpty_failsWhenEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{}).isNotEmpty());
    }

    @Test
    void hasLength_failsOnMismatch() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{1.0}).hasLength(5));
    }

    @Test
    void contains_failsWhenMissing() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{1.0, 2.0}).contains(9.0));
    }

    @Test
    void doesNotContain_failsWhenPresent() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{1.0, 2.0}).doesNotContain(2.0));
    }

    @Test
    void containsCloseTo_failsWhenNotFound() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{1.0, 2.0}).containsCloseTo(99.0, 0.001));
    }

    @Test
    void containsExactly_failsOnMismatch() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{1.0, 2.0}).containsExactly(2.0, 1.0));
    }

    @Test
    void containsExactlyInAnyOrder_failsOnWrongElements() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{1.0, 2.0}).containsExactlyInAnyOrder(3.0, 4.0));
    }

    @Test
    void isSorted_failsWhenUnsorted() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{3.0, 1.0}).isSorted());
    }

    @Test
    void allMatch_failsWhenOneFails() {
        assertThrows(AssertionError.class,
                () -> checkThat(new double[]{1.0, -2.0}).allMatch(d -> d > 0, "positive"));
    }

    @Test
    void containsExactlyInAnyOrder_fails_on_wrong_multiset() {
        assertThrows(
                AssertionError.class,
                () -> checkThat(new double[]{1.0, 1.0, 2.0}).containsExactlyInAnyOrder(1.0, 2.0, 2.0));
    }

    @Test
    void all_methods_pass_on_valid_input() {
        assertDoesNotThrow(() -> checkThat(new double[]{1.0, 2.0, 3.0})
                .isNotEmpty()
                .hasLength(3)
                .contains(2.0)
                .containsCloseTo(2.0, 0.001)
                .doesNotContain(9.0)
                .containsExactly(1.0, 2.0, 3.0)
                .containsExactlyInAnyOrder(3.0, 1.0, 2.0)
                .isSorted()
                .allMatch(d -> d > 0, "positive"));
    }
}
