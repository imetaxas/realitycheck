package com.yanimetaxas.realitycheck;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Fluent assertions for {@link String} values.
 */
public final class StringCheck extends AbstractCheck<StringCheck, String> {

    StringCheck(String actual, FailureHandler handler) {
        super(actual, handler);
    }

    public StringCheck isEmpty() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().isEmpty(),
                "expected an empty string but was: <%s> (length %d)",
                actual(), actual().length());
    }

    public StringCheck isNotEmpty() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), !actual().isEmpty(),
                "expected a non-empty string");
    }

    public StringCheck isBlank() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().isBlank(),
                "expected a blank string but was: <%s>", actual());
    }

    public StringCheck isNotBlank() {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), !actual().isBlank(),
                "expected a non-blank string");
    }

    public StringCheck hasLength(int expected) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().length() == expected,
                "expected length <%d> but was <%d> for string: <%s>",
                expected, actual().length(), actual());
    }

    public StringCheck contains(String substring) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().contains(substring),
                "expected string containing <%s> but was: <%s>",
                substring, actual());
    }

    public StringCheck doesNotContain(String substring) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), !actual().contains(substring),
                "expected string not containing <%s> but was: <%s>",
                substring, actual());
    }

    public StringCheck startsWith(String prefix) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().startsWith(prefix),
                "expected string starting with <%s> but was: <%s>",
                prefix, actual());
    }

    public StringCheck endsWith(String suffix) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().endsWith(suffix),
                "expected string ending with <%s> but was: <%s>",
                suffix, actual());
    }

    public StringCheck matches(String regex) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().matches(regex),
                "expected string matching /%s/ but was: <%s>",
                regex, actual());
    }

    public StringCheck doesNotMatch(String regex) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), !actual().matches(regex),
                "expected string not matching /%s/ but was: <%s>",
                regex, actual());
    }

    public StringCheck matchesPattern(Pattern pattern) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), pattern.matcher(actual()).matches(),
                "expected string matching pattern <%s> but was: <%s>",
                pattern, actual());
    }

    public StringCheck containsIgnoringCase(String substring) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(),
                actual().toLowerCase(Locale.ROOT).contains(substring.toLowerCase(Locale.ROOT)),
                "expected string containing (ignoring case) <%s> but was: <%s>",
                substring, actual());
    }

    public StringCheck isEqualToIgnoringCase(String expected) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().equalsIgnoreCase(expected),
                "expected (ignoring case): <%s> but was: <%s>",
                expected, actual());
    }

    public StringCheck hasLengthGreaterThan(int minLength) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().length() > minLength,
                "expected length greater than <%d> but was <%d>",
                minLength, actual().length());
    }

    public StringCheck hasLengthLessThan(int maxLength) {
        if (!isActualPresent()) return self();
        return failureHandler().check(self(), actual().length() < maxLength,
                "expected length less than <%d> but was <%d>",
                maxLength, actual().length());
    }

    public StringCheck hasLengthBetween(int minInclusive, int maxInclusive) {
        if (!isActualPresent()) return self();
        int len = actual().length();
        return failureHandler().check(self(),
                len >= minInclusive && len <= maxInclusive,
                "expected length between <%d> and <%d> but was <%d>",
                minInclusive, maxInclusive, len);
    }

    /**
     * Matches the string against a regex and returns a {@link MatchResultCheck}
     * for asserting on captured groups.
     *
     * <pre>{@code
     * checkThat(logLine).matchesAndCaptures("(\\w+) (\\d+)")
     *     .hasGroupCount(2)
     *     .group(1).isEqualTo("ERROR");
     * }</pre>
     */
    public MatchResultCheck matchesAndCaptures(String regex) {
        if (!isActualPresent()) return MatchResultCheck.from("", regex, failureHandler());
        return MatchResultCheck.from(actual(), regex, failureHandler());
    }
}
