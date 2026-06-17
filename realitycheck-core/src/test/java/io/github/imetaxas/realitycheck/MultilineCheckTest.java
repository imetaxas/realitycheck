package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThatMultiline;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MultilineCheckTest {

    private static final String LOG_OUTPUT = """
            INFO  Starting server on port 8080
            DEBUG Loading configuration from /etc/app.conf
            INFO  Server ready, accepting connections
            WARN  High memory usage detected: 85%
            ERROR Connection pool exhausted""";

    @Test
    void hasLineCount_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT).hasLineCount(5));
    }

    @Test
    void hasLineCount_fails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThatMultiline(LOG_OUTPUT).hasLineCount(3));
        assertTrue(e.getMessage().contains("3"));
        assertTrue(e.getMessage().contains("5"));
    }

    @Test
    void line_extraction_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT)
                .line(0).startsWith("INFO"));
    }

    @Test
    void firstLine_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT)
                .firstLine().contains("Starting server"));
    }

    @Test
    void lastLine_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT)
                .lastLine().contains("Connection pool"));
    }

    @Test
    void containsLine_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT)
                .containsLine("WARN  High memory usage detected: 85%"));
    }

    @Test
    void containsLineMatching_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT)
                .containsLineMatching(".*memory usage.*\\d+%"));
    }

    @Test
    void containsLineContaining_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT)
                .containsLineContaining("8080"));
    }

    @Test
    void allLinesMatch_passes() {
        String numbered = "line 1\nline 2\nline 3";
        assertDoesNotThrow(() -> checkThatMultiline(numbered)
                .allLinesMatch("line \\d+"));
    }

    @Test
    void noLineMatches_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT)
                .noLineMatches(".*FATAL.*"));
    }

    @Test
    void noLineMatches_fails() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline(LOG_OUTPUT).noLineMatches(".*ERROR.*"));
    }

    @Test
    void isEqualTo_showsDiff() {
        String expected = """
                INFO  Starting server on port 8080
                DEBUG Loading configuration from /etc/app.conf
                INFO  Server ready, accepting connections""";

        var e = assertThrows(AssertionError.class,
                () -> checkThatMultiline(LOG_OUTPUT).isEqualTo(expected));
        assertTrue(e.getMessage().contains("differs"));
    }

    @Test
    void isEqualTo_passes() {
        assertDoesNotThrow(() -> checkThatMultiline("hello\nworld").isEqualTo("hello\nworld"));
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT).isNotEmpty());
    }

    @Test
    void isEmpty_fails() {
        assertThrows(AssertionError.class, () -> checkThatMultiline(LOG_OUTPUT).isEmpty());
    }

    @Test
    void hasLineCountGreaterThan_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT).hasLineCountGreaterThan(3));
    }

    @Test
    void hasLineCountLessThan_passes() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT).hasLineCountLessThan(10));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() -> checkThatMultiline(LOG_OUTPUT)
                .isNotEmpty()
                .hasLineCount(5)
                .containsLineContaining("8080")
                .noLineMatches(".*FATAL.*"));
    }

    @Test
    void line_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("line1\nline2\nline3").line(1).isEqualTo("line2"));
    }

    @Test
    void line_outOfRange() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("line1\nline2").line(99));
    }

    @Test
    void line_negativeIndex() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("line1").line(-1));
    }

    @Test
    void lastLine_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("first\nsecond\nthird").lastLine().isEqualTo("third"));
    }

    @Test
    void lastLine_emptyString() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("").lastLine());
    }

    @Test
    void isEqualToNormalized_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("hello  \nworld  ")
                        .isEqualToNormalized("hello\nworld"));
    }

    @Test
    void isEqualToNormalized_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("hello\nworld")
                        .isEqualToNormalized("hello\nEARTH"));
    }

    @Test
    void isEqualTo_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("hello\nworld").isEqualTo("hello\nworld"));
    }

    @Test
    void isEqualTo_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("hello\nworld").isEqualTo("hello\nearth"));
    }

    @Test
    void hasLineCount_pass() {
        assertDoesNotThrow(() -> checkThatMultiline("a\nb\nc").hasLineCount(3));
    }

    @Test
    void hasLineCount_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("a\nb").hasLineCount(5));
    }

    @Test
    void firstLine_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("first\nsecond").firstLine().isEqualTo("first"));
    }

    @Test
    void containsLine_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("a\nb\nc").containsLine("b"));
    }

    @Test
    void containsLine_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("a\nb").containsLine("z"));
    }

    @Test
    void containsLineMatching_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("abc\n123").containsLineMatching("\\d+"));
    }

    @Test
    void containsLineMatching_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("abc\ndef").containsLineMatching("^\\d+$"));
    }

    @Test
    void allLinesMatch_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("abc\ndef").allLinesMatch("[a-z]+"));
    }

    @Test
    void allLinesMatch_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("abc\n123").allLinesMatch("[a-z]+"));
    }

    @Test
    void noLineMatches_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("abc\ndef").noLineMatches("\\d+"));
    }

    @Test
    void noLineMatches_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("abc\n123").noLineMatches("\\d+"));
    }

    @Test
    void hasLineCountGreaterThan_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("a\nb\nc").hasLineCountGreaterThan(2));
    }

    @Test
    void hasLineCountGreaterThan_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("a\nb").hasLineCountGreaterThan(2));
    }

    @Test
    void hasLineCountLessThan_pass() {
        assertDoesNotThrow(() ->
                checkThatMultiline("a\nb").hasLineCountLessThan(5));
    }

    @Test
    void hasLineCountLessThan_fail() {
        assertThrows(AssertionError.class, () ->
                checkThatMultiline("a\nb\nc").hasLineCountLessThan(3));
    }

    @Test
    void containsLineMatching_failsWhenNoLineMatches() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline("abc\ndef").containsLineMatching("^\\d+$"));
    }

    @Test
    void containsLineContaining_failsWhenMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline("abc\ndef").containsLineContaining("zzz"));
    }

    @Test
    void allLinesMatch_failsWhenSomeDont() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline("abc\n123").allLinesMatch("[a-z]+"));
    }

    @Test
    void firstLine_extractorChains() {
        assertDoesNotThrow(
                () -> checkThatMultiline("first\nsecond").firstLine().isEqualTo("first"));
    }

    @Test
    void hasLineCount_failsOnWrongCount() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline("a\nb\nc").hasLineCount(99));
    }

    @Test
    void containsLine_failsWhenMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline("a\nb\nc").containsLine("zzz"));
    }

    @Test
    void isEqualToNormalized_failsWhenDifferent() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline("hello\nworld")
                        .isEqualToNormalized("hello\nEARTH"));
    }

    @Test
    void isEmpty_failsWhenNonEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline("content").isEmpty());
    }

    @Test
    void isNotEmpty_failsWhenEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline("").isNotEmpty());
    }

    @Test
    void noLineMatches_failsWhenOneMatches() {
        assertThrows(AssertionError.class,
                () -> checkThatMultiline("abc\n123").noLineMatches("\\d+"));
    }

    @Test
    void line_negativeIndex_softMode_returnsEmptyString() {
        // In strict mode fail() throws before the defensive ternary in line() runs.
        // Soft mode exercises the `index >= 0 && index < size` false branches.
        var handler = new SoftFailureHandler();
        MultilineCheck check = new MultilineCheck("a\nb", handler);
        StringCheck result = check.line(-1);
        assertEquals("", result.actual());
        assertEquals(1, handler.failures().size());
    }

    @Test
    void line_outOfRangePositive_softMode_returnsEmptyString() {
        var handler = new SoftFailureHandler();
        MultilineCheck check = new MultilineCheck("a\nb", handler);
        StringCheck result = check.line(999);
        assertEquals("", result.actual());
        assertEquals(1, handler.failures().size());
    }
}
