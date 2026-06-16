package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LongArrayCheckTest {

    @Test
    void hasLength_passes() {
        assertDoesNotThrow(() -> checkThat(new long[]{1L, 2L}).hasLength(2));
    }

    @Test
    void contains_passes() {
        assertDoesNotThrow(() -> checkThat(new long[]{100L, 200L, 300L}).contains(200L));
    }

    @Test
    void containsExactly_passes() {
        assertDoesNotThrow(() ->
                checkThat(new long[]{1L, 2L, 3L}).containsExactly(1L, 2L, 3L));
    }

    @Test
    void containsExactlyInAnyOrder_passes() {
        assertDoesNotThrow(() ->
                checkThat(new long[]{3L, 1L, 2L}).containsExactlyInAnyOrder(1L, 2L, 3L));
    }

    @Test
    void isSorted_passes() {
        assertDoesNotThrow(() -> checkThat(new long[]{1L, 2L, 3L}).isSorted());
    }

    @Test
    void isSorted_fails() {
        assertThrows(AssertionError.class, () -> checkThat(new long[]{3L, 1L}).isSorted());
    }

    @Test
    void allMatch_passes() {
        assertDoesNotThrow(() ->
                checkThat(new long[]{2L, 4L}).allMatch(n -> n % 2 == 0, "even"));
    }

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(new long[]{}).isEmpty());
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(new long[]{1L}).isNotEmpty());
    }

    @Test
    void allMatch_pass() {
        assertDoesNotThrow(() ->
                checkThat(new long[]{2L, 4L, 6L}).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void allMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new long[]{2L, 3L, 6L}).allMatch(x -> x % 2 == 0, "even"));
    }

    @Test
    void containsExactly_pass() {
        assertDoesNotThrow(() ->
                checkThat(new long[]{1L, 2L, 3L}).containsExactly(1L, 2L, 3L));
    }

    @Test
    void containsExactly_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new long[]{1L, 2L, 3L}).containsExactly(1L, 2L, 4L));
    }

    @Test
    void isSorted_pass() {
        assertDoesNotThrow(() -> checkThat(new long[]{1L, 2L, 3L}).isSorted());
    }

    @Test
    void isSorted_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new long[]{3L, 1L, 2L}).isSorted());
    }

    @Test
    void containsExactlyInAnyOrder_pass() {
        assertDoesNotThrow(() ->
                checkThat(new long[]{3L, 1L, 2L}).containsExactlyInAnyOrder(1L, 2L, 3L));
    }

    @Test
    void containsExactlyInAnyOrder_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new long[]{1L, 2L}).containsExactlyInAnyOrder(4L, 5L));
    }

    @Test
    void contains_pass() {
        assertDoesNotThrow(() -> checkThat(new long[]{1L, 2L, 3L}).contains(2L));
    }

    @Test
    void contains_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new long[]{1L, 2L}).contains(9L));
    }

    @Test
    void doesNotContain_pass() {
        assertDoesNotThrow(() -> checkThat(new long[]{1L, 2L}).doesNotContain(9L));
    }

    @Test
    void doesNotContain_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new long[]{1L, 2L}).doesNotContain(2L));
    }

    @Test
    void isEmpty_failsWhenNotEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{1L}).isEmpty());
    }

    @Test
    void isNotEmpty_failsWhenEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{}).isNotEmpty());
    }

    @Test
    void hasLength_failsOnMismatch() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{1L, 2L}).hasLength(5));
    }

    @Test
    void contains_failsWhenMissing() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{1L, 2L}).contains(99L));
    }

    @Test
    void doesNotContain_failsWhenPresent() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{1L, 2L}).doesNotContain(2L));
    }

    @Test
    void containsExactly_failsOnWrongOrder() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{1L, 2L}).containsExactly(2L, 1L));
    }

    @Test
    void containsExactlyInAnyOrder_failsOnWrongElements() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{1L, 2L}).containsExactlyInAnyOrder(3L, 4L));
    }

    @Test
    void isSorted_failsWhenUnsorted() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{3L, 1L}).isSorted());
    }

    @Test
    void allMatch_failsWhenOneFails() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{2L, 3L}).allMatch(n -> n % 2 == 0, "even"));
    }

    @Test
    void containsExactlyInAnyOrder_fails_on_wrong_multiset() {
        assertThrows(AssertionError.class,
                () -> checkThat(new long[]{1L, 1L, 2L}).containsExactlyInAnyOrder(1L, 2L, 2L));
    }

    @Test
    void all_methods_pass_on_valid_input() {
        assertDoesNotThrow(() -> checkThat(new long[]{1L, 2L, 3L})
                .isNotEmpty()
                .hasLength(3)
                .contains(2L)
                .doesNotContain(9L)
                .containsExactly(1L, 2L, 3L)
                .containsExactlyInAnyOrder(3L, 1L, 2L)
                .isSorted()
                .allMatch(n -> n > 0, "positive"));
    }
}
