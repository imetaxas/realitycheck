package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static com.yanimetaxas.realitycheck.Reality.checkWithMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class StringCheckTest {

    @Test
    void isNotNull_passes() {
        assertDoesNotThrow(() -> checkThat("hello").isNotNull());
    }

    @Test
    void isNull_failsWithMessage() {
        var e = assertThrows(AssertionError.class, () -> checkThat("hello").isNull());
        assertTrue(e.getMessage().contains("hello"), "should include actual value");
    }

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThat("").isEmpty());
    }

    @Test
    void isEmpty_failsWithDetail() {
        var e = assertThrows(AssertionError.class, () -> checkThat("abc").isEmpty());
        assertTrue(e.getMessage().contains("length 3"));
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThat("x").isNotEmpty());
    }

    @Test
    void isBlank_passes() {
        assertDoesNotThrow(() -> checkThat("  ").isBlank());
    }

    @Test
    void hasLength_passes() {
        assertDoesNotThrow(() -> checkThat("hello").hasLength(5));
    }

    @Test
    void hasLength_failsWithExpectedAndActual() {
        var e = assertThrows(AssertionError.class, () -> checkThat("hello").hasLength(3));
        assertTrue(e.getMessage().contains("3"), "should include expected length");
        assertTrue(e.getMessage().contains("5"), "should include actual length");
    }

    @Test
    void contains_passes() {
        assertDoesNotThrow(() -> checkThat("hello world").contains("world"));
    }

    @Test
    void contains_fails() {
        assertThrows(AssertionError.class, () -> checkThat("hello").contains("xyz"));
    }

    @Test
    void doesNotContain_passes() {
        assertDoesNotThrow(() -> checkThat("hello").doesNotContain("xyz"));
    }

    @Test
    void startsWith_passes() {
        assertDoesNotThrow(() -> checkThat("hello").startsWith("hel"));
    }

    @Test
    void endsWith_passes() {
        assertDoesNotThrow(() -> checkThat("hello").endsWith("llo"));
    }

    @Test
    void matches_passes() {
        assertDoesNotThrow(() -> checkThat("abc123").matches("[a-z]+\\d+"));
    }

    @Test
    void isEqualToIgnoringCase_passes() {
        assertDoesNotThrow(() -> checkThat("Hello").isEqualToIgnoringCase("HELLO"));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat("hello").isNotNull().isNotEmpty().hasLength(5).startsWith("he").endsWith("lo"));
    }

    @Test
    void customMessage_overridesDefault() {
        var e = assertThrows(AssertionError.class, () ->
                checkWithMessage("Name is too short").that("ab").hasLength(5));
        assertTrue(e.getMessage().contains("Name is too short"));
    }

    @Test
    void hasLengthBetween_passes() {
        assertDoesNotThrow(() -> checkThat("hello").hasLengthBetween(3, 10));
    }

    @Test
    void hasLengthBetween_fails() {
        var e = assertThrows(AssertionError.class, () -> checkThat("hi").hasLengthBetween(5, 10));
        assertTrue(e.getMessage().contains("5"));
        assertTrue(e.getMessage().contains("10"));
    }

    @Test
    void containsIgnoringCase_passes() {
        assertDoesNotThrow(() -> checkThat("Hello World").containsIgnoringCase("HELLO"));
    }

    @Test
    void doesNotMatch_passes() {
        assertDoesNotThrow(() -> checkThat("hello").doesNotMatch("\\d+"));
    }

    @Test
    void isEqualTo_withDifferentValues_showsBoth() {
        var e = assertThrows(AssertionError.class, () -> checkThat("foo").isEqualTo("bar"));
        assertEquals("expected: <bar> but was: <foo>", e.getMessage());
    }

    @Test
    void matchesPattern_pass() {
        assertDoesNotThrow(() ->
                checkThat("abc123").matchesPattern(Pattern.compile("[a-z]+\\d+")));
    }

    @Test
    void matchesPattern_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat("NOPE").matchesPattern(Pattern.compile("^\\d+$")));
    }

    @Test
    void hasLengthGreaterThan_pass() {
        assertDoesNotThrow(() -> checkThat("hello").hasLengthGreaterThan(3));
    }

    @Test
    void hasLengthGreaterThan_fail_equal() {
        assertThrows(AssertionError.class, () ->
                checkThat("hello").hasLengthGreaterThan(5));
    }

    @Test
    void hasLengthGreaterThan_fail_less() {
        assertThrows(AssertionError.class, () ->
                checkThat("hi").hasLengthGreaterThan(5));
    }

    @Test
    void hasLengthLessThan_pass() {
        assertDoesNotThrow(() -> checkThat("hi").hasLengthLessThan(5));
    }

    @Test
    void hasLengthLessThan_fail_equal() {
        assertThrows(AssertionError.class, () ->
                checkThat("hello").hasLengthLessThan(5));
    }

    @Test
    void hasLengthLessThan_fail_greater() {
        assertThrows(AssertionError.class, () ->
                checkThat("hello world").hasLengthLessThan(5));
    }

    @Test
    void hasLengthBetween_pass() {
        assertDoesNotThrow(() -> checkThat("hello").hasLengthBetween(3, 7));
    }

    @Test
    void hasLengthBetween_fail_below() {
        assertThrows(AssertionError.class, () ->
                checkThat("hi").hasLengthBetween(5, 10));
    }

    @Test
    void hasLengthBetween_fail_above() {
        assertThrows(AssertionError.class, () ->
                checkThat("hello world!!").hasLengthBetween(1, 5));
    }

    @Test
    void containsIgnoringCase_pass() {
        assertDoesNotThrow(() -> checkThat("Hello World").containsIgnoringCase("HELLO"));
    }

    @Test
    void containsIgnoringCase_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat("Hello World").containsIgnoringCase("xyz"));
    }

    @Test
    void isEqualToIgnoringCase_pass() {
        assertDoesNotThrow(() -> checkThat("Hello").isEqualToIgnoringCase("HELLO"));
    }

    @Test
    void isEqualToIgnoringCase_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat("Hello").isEqualToIgnoringCase("World"));
    }

    @Test
    void doesNotMatch_pass() {
        assertDoesNotThrow(() -> checkThat("hello").doesNotMatch("\\d+"));
    }

    @Test
    void doesNotMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat("123").doesNotMatch("\\d+"));
    }

    @Test
    void startsWith_fails() {
        assertThrows(AssertionError.class, () -> checkThat("hello").startsWith("xyz"));
    }

    @Test
    void matches_fails() {
        assertThrows(AssertionError.class, () -> checkThat("hello").matches("\\d+"));
    }

    @Test
    void isNotEmpty_fails() {
        assertThrows(AssertionError.class, () -> checkThat("").isNotEmpty());
    }

    @Test
    void doesNotContain_fails() {
        assertThrows(AssertionError.class, () -> checkThat("hello").doesNotContain("ell"));
    }

    @Test
    void endsWith_fails() {
        assertThrows(AssertionError.class, () -> checkThat("hello").endsWith("xyz"));
    }

    @Test
    void doesNotMatch_fails() {
        assertThrows(AssertionError.class, () -> checkThat("123").doesNotMatch("\\d+"));
    }

    @Test
    void containsIgnoringCase_fails() {
        assertThrows(AssertionError.class, () -> checkThat("Hello").containsIgnoringCase("xyz"));
    }

    @Test
    void isEqualToIgnoringCase_fails() {
        assertThrows(AssertionError.class, () -> checkThat("Hello").isEqualToIgnoringCase("World"));
    }

    @Test
    void hasLengthGreaterThan_passes() {
        assertDoesNotThrow(() -> checkThat("hello").hasLengthGreaterThan(3));
    }

    @Test
    void hasLengthGreaterThan_fails() {
        assertThrows(AssertionError.class, () -> checkThat("hi").hasLengthGreaterThan(5));
    }

    @Test
    void hasLengthLessThan_passes() {
        assertDoesNotThrow(() -> checkThat("hi").hasLengthLessThan(5));
    }

    @Test
    void hasLengthLessThan_fails() {
        assertThrows(AssertionError.class, () -> checkThat("hello").hasLengthLessThan(3));
    }

    @Test
    void hasLengthBetween_fails_tooShort() {
        assertThrows(AssertionError.class, () -> checkThat("hi").hasLengthBetween(5, 10));
    }

    @Test
    void hasLengthBetween_fails_tooLong() {
        assertThrows(AssertionError.class, () -> checkThat("hello world").hasLengthBetween(1, 5));
    }

    @Test
    void isEmpty_fails() {
        assertThrows(AssertionError.class, () -> checkThat("x").isEmpty());
    }

    @Test
    void isBlank_fails() {
        assertThrows(AssertionError.class, () -> checkThat("text").isBlank());
    }

    @Test
    void isNotBlank_passes() {
        assertDoesNotThrow(() -> checkThat("text").isNotBlank());
    }

    @Test
    void isNotBlank_fails() {
        assertThrows(AssertionError.class, () -> checkThat("  ").isNotBlank());
    }

    @Test
    void matchesAndCaptures_passesWithGroups() {
        assertDoesNotThrow(() ->
                checkThat("ERROR 42")
                        .matchesAndCaptures("(\\w+) (\\d+)")
                        .hasGroupCount(2)
                        .group(1)
                        .isEqualTo("ERROR"));
    }

    @Test
    void matchesAndCaptures_failsWhenStringDoesNotMatchRegex() {
        assertThrows(
                AssertionError.class,
                () -> checkThat("no digits").matchesAndCaptures("(\\d+)").hasGroupCount(1));
    }
}
