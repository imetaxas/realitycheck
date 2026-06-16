package io.github.imetaxas.realitycheck;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Fluent assertions for {@link Optional} values.
 *
 * @param <T> the contained type
 */
public final class OptionalCheck<T> extends AbstractCheck<OptionalCheck<T>, Optional<T>> {

    OptionalCheck(Optional<T> actual, FailureHandler handler) {
        super(actual, handler);
    }

    public OptionalCheck<T> isPresent() {
        return failureHandler().check(self(), actual().isPresent(),
                "expected Optional to be present but was empty");
    }

    public OptionalCheck<T> isEmpty() {
        return failureHandler().check(self(), actual().isEmpty(),
                "expected empty Optional but had value: <%s>",
                actual().isPresent() ? actual().get() : "");
    }

    public OptionalCheck<T> hasValue(T expected) {
        if (actual().isEmpty()) {
            failureHandler().fail("expected Optional with value <%s> but was empty",
                    expected);
        } else if (!Objects.equals(actual().get(), expected)) {
            failureHandler().fail("expected Optional value <%s> but was: <%s>",
                    expected, actual().get());
        }
        return self();
    }

    /**
     * Extracts the value for further assertions. Fails if empty.
     */
    public ObjectCheck<T> value() {
        if (actual().isEmpty()) {
            failureHandler().fail("cannot extract value from empty Optional");
            return new ObjectCheck<>(null, failureHandler());
        }
        return new ObjectCheck<>(actual().get(), failureHandler());
    }

    /**
     * If present, passes the value to the consumer for custom assertions.
     */
    public OptionalCheck<T> ifPresent(Consumer<T> assertions) {
        actual().ifPresent(assertions);
        return self();
    }
}
