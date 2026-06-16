package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThatEnum;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.Month;
import org.junit.jupiter.api.Test;

class EnumCheckTest {

    enum Status { ACTIVE, INACTIVE, PENDING }

    @Test
    void hasName_passes() {
        assertDoesNotThrow(() -> checkThatEnum(Status.ACTIVE).hasName("ACTIVE"));
    }

    @Test
    void hasName_fails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatEnum(Status.ACTIVE).hasName("INACTIVE"));
        assertTrue(e.getMessage().contains("INACTIVE"));
        assertTrue(e.getMessage().contains("ACTIVE"));
    }

    @Test
    void hasOrdinal_passes() {
        assertDoesNotThrow(() -> checkThatEnum(Status.ACTIVE).hasOrdinal(0));
        assertDoesNotThrow(() -> checkThatEnum(Status.PENDING).hasOrdinal(2));
    }

    @Test
    void isOneOf_passes() {
        assertDoesNotThrow(() ->
                checkThatEnum(Status.ACTIVE).isOneOf(Status.ACTIVE, Status.PENDING));
    }

    @Test
    void isOneOf_fails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatEnum(Status.INACTIVE).isOneOf(Status.ACTIVE, Status.PENDING));
        assertTrue(e.getMessage().contains("INACTIVE"));
    }

    @Test
    void isNoneOf_passes() {
        assertDoesNotThrow(() ->
                checkThatEnum(Status.ACTIVE).isNoneOf(Status.INACTIVE, Status.PENDING));
    }

    @Test
    void isNoneOf_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatEnum(Status.ACTIVE).isNoneOf(Status.ACTIVE, Status.PENDING));
    }

    @Test
    void name_extractor_works() {
        assertDoesNotThrow(() ->
                checkThatEnum(Status.ACTIVE).name().startsWith("ACT"));
    }

    @Test
    void worksWithStandardEnums() {
        assertDoesNotThrow(() -> checkThatEnum(DayOfWeek.MONDAY).hasName("MONDAY").hasOrdinal(0));
        assertDoesNotThrow(() -> checkThatEnum(Month.JANUARY).hasOrdinal(0));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() -> checkThatEnum(Status.ACTIVE)
                .isNotNull()
                .hasName("ACTIVE")
                .hasOrdinal(0)
                .isOneOf(Status.ACTIVE, Status.PENDING));
    }

    @Test
    void hasOrdinal_pass() {
        assertDoesNotThrow(() -> checkThatEnum(Thread.State.NEW).hasOrdinal(0));
    }

    @Test
    void hasOrdinal_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatEnum(Thread.State.NEW).hasOrdinal(5));
    }

    @Test
    void isOneOf_pass() {
        assertDoesNotThrow(() ->
                checkThatEnum(Thread.State.NEW).isOneOf(Thread.State.NEW, Thread.State.RUNNABLE));
    }

    @Test
    void isOneOf_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatEnum(Thread.State.NEW)
                        .isOneOf(Thread.State.BLOCKED, Thread.State.TERMINATED));
    }

    @Test
    void isNoneOf_pass() {
        assertDoesNotThrow(() ->
                checkThatEnum(Thread.State.NEW)
                        .isNoneOf(Thread.State.BLOCKED, Thread.State.TERMINATED));
    }

    @Test
    void isNoneOf_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatEnum(Thread.State.NEW)
                        .isNoneOf(Thread.State.NEW, Thread.State.TERMINATED));
    }

    @Test
    void hasName_pass() {
        assertDoesNotThrow(() -> checkThatEnum(Thread.State.NEW).hasName("NEW"));
    }

    @Test
    void hasName_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatEnum(Thread.State.NEW).hasName("OLD"));
    }

    @Test
    void name_extractor() {
        assertDoesNotThrow(() ->
                checkThatEnum(Thread.State.NEW).name().isEqualTo("NEW"));
    }

    @Test
    void hasName_failsOnWrongName() {
        assertThrows(AssertionError.class,
                () -> checkThatEnum(Thread.State.NEW).hasName("BLOCKED"));
    }

    @Test
    void hasOrdinal_failsOnWrongOrdinal() {
        assertThrows(AssertionError.class,
                () -> checkThatEnum(Thread.State.NEW).hasOrdinal(99));
    }

    @Test
    void isOneOf_failsWhenNotIncluded() {
        assertThrows(AssertionError.class,
                () -> checkThatEnum(Thread.State.NEW)
                        .isOneOf(Thread.State.BLOCKED, Thread.State.TERMINATED));
    }

    @Test
    void isNoneOf_failsWhenIncluded() {
        assertThrows(AssertionError.class,
                () -> checkThatEnum(Thread.State.NEW)
                        .isNoneOf(Thread.State.NEW, Thread.State.TERMINATED));
    }

    @Test
    void name_extractorChains() {
        assertDoesNotThrow(
                () -> checkThatEnum(Thread.State.NEW).name().isEqualTo("NEW"));
    }

    @Test
    void name_extractorFails() {
        assertThrows(AssertionError.class,
                () -> checkThatEnum(Thread.State.NEW).name().isEqualTo("WRONG"));
    }
}
