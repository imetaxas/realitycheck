package io.github.imetaxas.realitycheck;

import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fluent assertions for regex {@link MatchResult} captures.
 * Created via {@code StringCheck.matchesAndCaptures(regex)}.
 *
 * <pre>{@code
 * checkThat(logLine).matchesAndCaptures("(\\w+) (\\d+) (.+)")
 *     .hasGroupCount(3)
 *     .group(1).isEqualTo("ERROR");
 *
 * checkThat(timestamp).matchesAndCaptures("(?<year>\\d{4})-(?<month>\\d{2})")
 *     .group("year").isEqualTo("2026");
 * }</pre>
 */
public final class MatchResultCheck extends AbstractCheck<MatchResultCheck, Matcher> {

    MatchResultCheck(Matcher actual, FailureHandler handler) {
        super(actual, handler);
    }

    public MatchResultCheck hasGroupCount(int expected) {
        int count = actual().groupCount();
        if (count != expected) {
            failureHandler().fail("expected <%d> capture groups but was: <%d>",
                    expected, count);
        }
        return self();
    }

    /** Extracts the numbered capture group as a {@link StringCheck}. */
    public StringCheck group(int index) {
        if (index < 0 || index > actual().groupCount()) {
            failureHandler().fail("group index <%d> out of range — matcher has <%d> groups",
                    index, actual().groupCount());
        }
        String value = (index >= 0 && index <= actual().groupCount()) ? actual().group(index) : null;
        return new StringCheck(value, failureHandler());
    }

    /** Extracts a named capture group as a {@link StringCheck}. */
    public StringCheck group(String name) {
        String value = null;
        try {
            value = actual().group(name);
        } catch (IllegalArgumentException e) {
            failureHandler().fail("no capture group named <%s> in pattern",
                    name);
        }
        return new StringCheck(value, failureHandler());
    }

    public MatchResultCheck hasGroup(int index, String expected) {
        if (index < 0 || index > actual().groupCount()) {
            failureHandler().fail("group index <%d> out of range — matcher has <%d> groups",
                    index, actual().groupCount());
        } else {
            String value = actual().group(index);
            if (!Objects.equals(expected, value)) {
                failureHandler().fail("expected group(%d) = <%s> but was: <%s>",
                        index, expected, value);
            }
        }
        return self();
    }

    public MatchResultCheck hasGroup(String name, String expected) {
        try {
            String value = actual().group(name);
            if (!Objects.equals(expected, value)) {
                failureHandler().fail("expected group(\"%s\") = <%s> but was: <%s>",
                        name, expected, value);
            }
        } catch (IllegalArgumentException e) {
            failureHandler().fail("no capture group named <%s> in pattern",
                    name);
        }
        return self();
    }

    static MatchResultCheck from(String input, String regex, FailureHandler handler) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            handler.fail("expected string to match /%s/ but was: <%s>", regex, input);
        }
        return new MatchResultCheck(matcher, handler);
    }
}
