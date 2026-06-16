package io.github.imetaxas.realitycheck;

import java.util.HashSet;
import java.util.Objects;

/**
 * Fluent assertions for exceptions. Created via {@code Reality.checkThatThrownBy(...)}.
 *
 * <pre>{@code
 * checkThatThrownBy(() -> service.process(null))
 *     .isInstanceOf(IllegalArgumentException.class)
 *     .hasMessageContaining("must not be null")
 *     .hasNoCause();
 *
 * checkThatThrownBy(() -> db.connect())
 *     .isInstanceOf(ConnectionException.class)
 *     .hasCauseInstanceOf(IOException.class)
 *     .rootCause().hasMessageContaining("refused");
 * }</pre>
 */
public final class ThrowableCheck extends AbstractCheck<ThrowableCheck, Throwable> {

    ThrowableCheck(Throwable actual, FailureHandler handler) {
        super(actual, handler);
    }

    public ThrowableCheck isExactlyInstanceOf(Class<? extends Throwable> type) {
        if (actual() == null) return self();
        return failureHandler().check(self(), actual().getClass().equals(type),
                "expected exactly <%s> but was: <%s>", type.getName(), actual().getClass().getName());
    }

    public ThrowableCheck hasMessage(String expected) {
        if (actual() == null) return self();
        return failureHandler().check(self(), Objects.equals(expected, actual().getMessage()),
                "expected message <%s> but was: <%s>", expected, actual().getMessage());
    }

    public ThrowableCheck hasMessageContaining(String substring) {
        if (actual() == null) return self();
        String msg = actual().getMessage();
        return failureHandler().check(self(), msg != null && msg.contains(substring),
                "expected message containing <%s> but was: <%s>", substring, msg);
    }

    public ThrowableCheck hasMessageStartingWith(String prefix) {
        if (actual() == null) return self();
        String msg = actual().getMessage();
        return failureHandler().check(self(), msg != null && msg.startsWith(prefix),
                "expected message starting with <%s> but was: <%s>", prefix, msg);
    }

    public ThrowableCheck hasMessageMatching(String regex) {
        if (actual() == null) return self();
        String msg = actual().getMessage();
        return failureHandler().check(self(), msg != null && msg.matches(regex),
                "expected message matching /%s/ but was: <%s>", regex, msg);
    }

    public ThrowableCheck hasNullMessage() {
        if (actual() == null) return self();
        return failureHandler().check(self(), actual().getMessage() == null,
                "expected null message but was: <%s>", actual().getMessage());
    }

    public ThrowableCheck hasCause() {
        if (actual() == null) return self();
        return failureHandler().check(self(), actual().getCause() != null,
                "expected exception to have a cause but it had none");
    }

    public ThrowableCheck hasNoCause() {
        if (actual() == null) return self();
        return failureHandler().check(self(), actual().getCause() == null,
                "expected exception to have no cause but cause was: <%s>",
                actual().getCause() != null ? actual().getCause().getClass().getName() : "");
    }

    public ThrowableCheck hasCauseInstanceOf(Class<? extends Throwable> type) {
        if (actual() == null) return self();
        if (actual().getCause() == null) {
            failureHandler().fail("expected cause of type <%s> but exception had no cause",
                    type.getName());
        } else if (!type.isInstance(actual().getCause())) {
            failureHandler().fail("expected cause of type <%s> but was: <%s>",
                    type.getName(), actual().getCause().getClass().getName());
        }
        return self();
    }

    public ThrowableCheck hasRootCauseInstanceOf(Class<? extends Throwable> type) {
        if (actual() == null) return self();
        Throwable root = rootCauseOf(actual());
        return failureHandler().check(self(), type.isInstance(root),
                "expected root cause of type <%s> but was: <%s>", type.getName(), root.getClass().getName());
    }

    /** Extracts the direct cause for further assertions. Fails if there is no cause. */
    public ThrowableCheck cause() {
        if (actual() == null) return self();
        if (actual().getCause() == null) {
            failureHandler().fail("cannot extract cause — exception has no cause");
            return new ThrowableCheck(null, failureHandler());
        }
        return new ThrowableCheck(actual().getCause(), failureHandler());
    }

    /** Extracts the root cause (deepest in the chain) for further assertions. */
    public ThrowableCheck rootCause() {
        if (actual() == null) return self();
        return new ThrowableCheck(rootCauseOf(actual()), failureHandler());
    }

    /** Extracts the message as a {@link StringCheck} for further assertions. */
    public StringCheck message() {
        if (actual() == null) return new StringCheck(null, failureHandler());
        return new StringCheck(actual().getMessage(), failureHandler());
    }

    // ── Suppressed exceptions ────────────────────────────────────────────

    public ThrowableCheck hasSuppressed() {
        if (actual() == null) return self();
        return failureHandler().check(self(), actual().getSuppressed().length > 0,
                "expected exception to have suppressed exceptions but it had none");
    }

    public ThrowableCheck hasNoSuppressed() {
        if (actual() == null) return self();
        Throwable[] suppressed = actual().getSuppressed();
        return failureHandler().check(self(), suppressed.length == 0,
                "expected no suppressed exceptions but had <%d>", suppressed.length);
    }

    public ThrowableCheck hasSuppressedCount(int expected) {
        if (actual() == null) return self();
        int count = actual().getSuppressed().length;
        return failureHandler().check(self(), count == expected,
                "expected <%d> suppressed exceptions but was: <%d>", expected, count);
    }

    public ThrowableCheck hasSuppressedInstanceOf(Class<? extends Throwable> type) {
        if (actual() == null) return self();
        boolean found = false;
        for (Throwable s : actual().getSuppressed()) {
            if (type.isInstance(s)) { found = true; break; }
        }
        return failureHandler().check(self(), found,
                "expected a suppressed exception of type <%s> but found none", type.getName());
    }

    /** Extracts a suppressed exception by index for further assertions. */
    public ThrowableCheck suppressedException(int index) {
        if (actual() == null) return self();
        Throwable[] suppressed = actual().getSuppressed();
        if (index < 0 || index >= suppressed.length) {
            failureHandler().fail("suppressed index <%d> out of range — exception has <%d> suppressed",
                    index, suppressed.length);
            return new ThrowableCheck(null, failureHandler());
        }
        return new ThrowableCheck(suppressed[index], failureHandler());
    }

    private static Throwable rootCauseOf(Throwable t) {
        Throwable root = t;
        HashSet<Throwable> visited = new HashSet<>();
        visited.add(root);
        while (root.getCause() != null && visited.add(root.getCause())) {
            root = root.getCause();
        }
        return root;
    }
}
