package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FloatArrayCheckTest {

    @Test
    void allAssertions_passForMatchingArray() {
        assertDoesNotThrow(() -> checkThat(new float[] {1.0f, 2.0f, 3.0f})
                .isNotEmpty()
                .hasLength(3)
                .contains(2.0f)
                .doesNotContain(9.0f)
                .containsExactly(1.0f, 2.0f, 3.0f)
                .isSorted()
                .allMatch(value -> value > 0, "positive")
                .allMatch(value -> value < 4)
                .anyMatch(value -> value == 2.0f, "two")
                .anyMatch(value -> value == 3.0f));
        assertDoesNotThrow(() -> checkThat(new float[0]).isEmpty().isSorted());
    }

    @Test
    void contains_usesFloatComparisonSemantics() {
        assertDoesNotThrow(() -> checkThat(new float[] {Float.NaN}).contains(Float.NaN));
        assertThrows(
                AssertionError.class,
                () -> checkThat(new float[] {0.0f}).contains(-0.0f));
    }

    @Test
    void isEmpty_failsForNonEmptyArray() {
        assertThrows(AssertionError.class, () -> checkThat(new float[] {1.0f}).isEmpty());
    }

    @Test
    void isNotEmpty_failsForEmptyArray() {
        assertThrows(AssertionError.class, () -> checkThat(new float[0]).isNotEmpty());
    }

    @Test
    void hasLength_reportsExpectedAndActualLengths() {
        AssertionError error = assertThrows(
                AssertionError.class,
                () -> checkThat(new float[] {1.0f}).hasLength(2));
        assertTrue(error.getMessage().contains("expected float[] length <2> but was: <1>"));
    }

    @Test
    void contains_failsWhenElementIsMissing() {
        assertThrows(
                AssertionError.class,
                () -> checkThat(new float[] {1.0f, 2.0f}).contains(3.0f));
    }

    @Test
    void doesNotContain_failsWhenElementIsPresent() {
        assertThrows(
                AssertionError.class,
                () -> checkThat(new float[] {1.0f, 2.0f}).doesNotContain(2.0f));
    }

    @Test
    void containsExactly_failsForDifferentOrderOrValues() {
        assertThrows(
                AssertionError.class,
                () -> checkThat(new float[] {1.0f, 2.0f}).containsExactly(2.0f, 1.0f));
    }

    @Test
    void isSorted_reportsFirstInversion() {
        AssertionError error = assertThrows(
                AssertionError.class,
                () -> checkThat(new float[] {1.0f, 3.0f, 2.0f}).isSorted());
        assertTrue(error.getMessage().contains("index <1>"));
        assertTrue(error.getMessage().contains("index <2>"));
    }

    @Test
    void allMatch_reportsFailingValueAndDescription() {
        AssertionError error = assertThrows(
                AssertionError.class,
                () -> checkThat(new float[] {1.0f, -2.0f})
                        .allMatch(value -> value > 0, "positive"));
        assertTrue(error.getMessage().contains("positive"));
        assertTrue(error.getMessage().contains("-2.0"));
    }

    @Test
    void anyMatch_reportsDescriptionWhenNoElementMatches() {
        AssertionError error = assertThrows(
                AssertionError.class,
                () -> checkThat(new float[] {1.0f, 2.0f})
                        .anyMatch(value -> value < 0, "negative"));
        assertTrue(error.getMessage().contains("negative"));
    }
}
