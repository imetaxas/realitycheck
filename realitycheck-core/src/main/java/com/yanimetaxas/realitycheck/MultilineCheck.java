package com.yanimetaxas.realitycheck;

import java.util.List;

/**
 * Fluent assertions for multiline strings. Uses the existing {@link Diff} engine
 * for line-by-line comparison.
 *
 * <pre>{@code
 * checkThatMultiline(output)
 *     .hasLineCount(5)
 *     .line(0).startsWith("HTTP/1.1")
 *     ;
 *
 * checkThatMultiline(actual).isEqualTo(expected);  // shows line-by-line diff on failure
 * }</pre>
 */
public final class MultilineCheck extends AbstractCheck<MultilineCheck, String> {

    private List<String> lines;

    MultilineCheck(String actual, FailureHandler handler) {
        super(actual, handler);
    }

    public MultilineCheck hasLineCount(int expected) {
        int count = lines().size();
        return failureHandler().check(self(), count == expected,
                "expected <%d> lines but was: <%d>", expected, count);
    }

    public MultilineCheck hasLineCountGreaterThan(int min) {
        int count = lines().size();
        return failureHandler().check(self(), count > min,
                "expected more than <%d> lines but was: <%d>", min, count);
    }

    public MultilineCheck hasLineCountLessThan(int max) {
        int count = lines().size();
        return failureHandler().check(self(), count < max,
                "expected fewer than <%d> lines but was: <%d>", max, count);
    }

    /** Extracts a specific line (0-indexed) as a {@link StringCheck}. */
    public StringCheck line(int index) {
        List<String> l = lines();
        if (index < 0 || index >= l.size()) {
            failureHandler().fail("line index <%d> out of range [0, %d)",
                    index, l.size());
        }
        String value = (index >= 0 && index < l.size()) ? l.get(index) : "";
        return new StringCheck(value, failureHandler());
    }

    /** Extracts the first line as a {@link StringCheck}. */
    public StringCheck firstLine() {
        return line(0);
    }

    /** Extracts the last line as a {@link StringCheck}. */
    public StringCheck lastLine() {
        return line(lines().size() - 1);
    }

    public MultilineCheck containsLine(String expected) {
        return failureHandler().check(self(), lines().contains(expected),
                "expected a line equal to <%s> but none found in %d lines",
                expected, lines().size());
    }

    public MultilineCheck containsLineMatching(String regex) {
        return failureHandler().check(self(),
                lines().stream().anyMatch(line -> line.matches(regex)),
                "expected a line matching /%s/ but none found in %d lines",
                regex, lines().size());
    }

    public MultilineCheck containsLineContaining(String substring) {
        return failureHandler().check(self(),
                lines().stream().anyMatch(line -> line.contains(substring)),
                "expected a line containing <%s> but none found in %d lines",
                substring, lines().size());
    }

    public MultilineCheck allLinesMatch(String regex) {
        for (int i = 0; i < lines().size(); i++) {
            if (!lines().get(i).matches(regex)) {
                failureHandler().fail("expected all lines to match /%s/ but line %d did not: <%s>",
                        regex, i, lines().get(i));
            }
        }
        return self();
    }

    public MultilineCheck noLineMatches(String regex) {
        for (int i = 0; i < lines().size(); i++) {
            if (lines().get(i).matches(regex)) {
                failureHandler().fail("expected no lines to match /%s/ but line %d did: <%s>",
                        regex, i, lines().get(i));
            }
        }
        return self();
    }

    public MultilineCheck isEmpty() {
        return failureHandler().check(self(), actual().isEmpty(),
                "expected empty content but had %d lines", lines().size());
    }

    public MultilineCheck isNotEmpty() {
        return failureHandler().check(self(), !actual().isEmpty(),
                "expected non-empty content");
    }

    /**
     * Compares against expected multiline content with a line-by-line diff on failure.
     * Uses the built-in {@link Diff} engine.
     */
    public MultilineCheck isEqualTo(String expected) {
        if (!actual().equals(expected)) {
            Diff.Result diff = Diff.of(expected, actual());
            failureHandler().fail("multiline content differs:\n%s", diff.format());
        }
        return self();
    }

    /**
     * Asserts that the content is equal to expected after trimming each line
     * and ignoring trailing whitespace differences.
     */
    public MultilineCheck isEqualToNormalized(String expected) {
        String normalizedActual = normalizeLines(actual());
        String normalizedExpected = normalizeLines(expected);
        if (!normalizedActual.equals(normalizedExpected)) {
            Diff.Result diff = Diff.of(normalizedExpected, normalizedActual);
            failureHandler().fail("multiline content differs (after normalization):\n%s",
                    diff.format());
        }
        return self();
    }

    private List<String> lines() {
        if (lines == null) {
            lines = actual().lines().toList();
        }
        return lines;
    }

    private static String normalizeLines(String text) {
        return String.join("\n", text.lines().map(String::stripTrailing).toList());
    }
}
