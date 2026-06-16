package io.github.imetaxas.realitycheck;

/**
 * Fluent assertions for {@link Enum} values.
 *
 * <pre>{@code
 * checkThat(status).hasName("ACTIVE");
 * checkThat(priority).isOneOf(Priority.HIGH, Priority.CRITICAL);
 * checkThat(dayOfWeek).hasOrdinal(0);
 * }</pre>
 *
 * @param <E> the enum type
 */
public final class EnumCheck<E extends Enum<E>> extends AbstractCheck<EnumCheck<E>, E> {

    EnumCheck(E actual, FailureHandler handler) {
        super(actual, handler);
    }

    public EnumCheck<E> hasName(String expected) {
        return failureHandler().check(self(), actual().name().equals(expected),
                "expected enum name <%s> but was: <%s>",
                expected, actual().name());
    }

    public EnumCheck<E> hasOrdinal(int expected) {
        return failureHandler().check(self(), actual().ordinal() == expected,
                "expected ordinal <%d> but was: <%d> for <%s>",
                expected, actual().ordinal(), actual());
    }

    @SafeVarargs
    public final EnumCheck<E> isOneOf(E... values) {
        boolean found = false;
        for (E v : values) {
            if (actual() == v) { found = true; break; }
        }
        if (!found) {
            failureHandler().fail("expected one of %s but was: <%s>",
                    java.util.Arrays.toString(values), actual());
        }
        return self();
    }

    @SafeVarargs
    public final EnumCheck<E> isNoneOf(E... values) {
        for (E v : values) {
            if (actual() == v) {
                failureHandler().fail("expected none of %s but was: <%s>",
                        java.util.Arrays.toString(values), actual());
            }
        }
        return self();
    }

    /** Extracts the enum name as a {@link StringCheck} for further assertions. */
    public StringCheck name() {
        return new StringCheck(actual().name(), failureHandler());
    }
}
