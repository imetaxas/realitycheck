package com.yanimetaxas.realitycheck;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Core check interface. All assertion types implement this.
 *
 * <p>Built-in checks extend {@link AbstractCheck}. Custom checks can implement
 * this interface directly (including as records) for zero-boilerplate extension:
 *
 * <pre>{@code
 * public record MoneyCheck(Money actual, FailureHandler failureHandler)
 *         implements Check<MoneyCheck, Money> {
 *     @Override public MoneyCheck self() { return this; }
 *
 *     public MoneyCheck hasCurrency(String code) {
 *         return failureHandler.check(self(),
 *             actual.getCurrency().equals(code),
 *             "expected currency <%s> but was <%s>", code, actual.getCurrency());
 *     }
 * }
 * }</pre>
 *
 * @param <SELF>   the self-referencing type for fluent chaining
 * @param <ACTUAL> the type of the value under test
 */
public interface Check<SELF extends Check<SELF, ACTUAL>, ACTUAL> {

    ACTUAL actual();

    SELF self();

    FailureHandler failureHandler();

    default SELF isNull() {
        return failureHandler().check(self(), actual() == null,
                "expected null but was: <%s>", actual());
    }

    default SELF isNotNull() {
        return failureHandler().check(self(), actual() != null,
                "expected a non-null value");
    }

    default SELF isEqualTo(ACTUAL expected) {
        return failureHandler().check(self(), Objects.equals(actual(), expected),
                "expected: <%s> but was: <%s>", expected, actual());
    }

    default SELF isNotEqualTo(ACTUAL expected) {
        return failureHandler().check(self(), !Objects.equals(actual(), expected),
                "expected a value different from: <%s>", expected);
    }

    default SELF isSameAs(ACTUAL expected) {
        return failureHandler().check(self(), actual() == expected,
                "expected same instance as: <%s> but was: <%s>", expected, actual());
    }

    default SELF isNotSameAs(ACTUAL expected) {
        return failureHandler().check(self(), actual() != expected,
                "expected a different instance from: <%s>", expected);
    }

    default SELF isInstanceOf(Class<?> type) {
        Objects.requireNonNull(type, "type must not be null");
        return failureHandler().check(self(), type.isInstance(actual()),
                "expected instance of <%s> but was <%s>",
                type.getName(),
                actual() == null ? "null" : actual().getClass().getName());
    }

    default SELF satisfies(Predicate<ACTUAL> condition, String description) {
        Objects.requireNonNull(condition, "condition must not be null");
        return failureHandler().check(self(), condition.test(actual()),
                "expected <%s> to satisfy: %s", actual(), description);
    }

    /**
     * Asserts that the value matches the given predicate.
     * Alias for {@link #satisfies(Predicate, String)} with a generic message.
     */
    default SELF matches(Predicate<ACTUAL> condition) {
        Objects.requireNonNull(condition, "condition must not be null");
        return failureHandler().check(self(), condition.test(actual()),
                "expected <%s> to match the given predicate", actual());
    }

    /**
     * Passes the actual value to the given consumer for arbitrary inline assertions.
     * Useful for one-off checks that don't warrant a custom check class.
     *
     * <pre>{@code
     * checkThat(person).satisfies(p -> {
     *     checkThat(p.name()).isNotEmpty();
     *     checkThat(p.age()).isPositive();
     * });
     * }</pre>
     */
    default SELF satisfies(java.util.function.Consumer<ACTUAL> assertions) {
        Objects.requireNonNull(assertions, "assertions must not be null");
        assertions.accept(actual());
        return self();
    }
}
