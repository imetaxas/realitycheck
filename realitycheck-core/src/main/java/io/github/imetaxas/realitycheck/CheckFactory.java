package io.github.imetaxas.realitycheck;

/**
 * Factory for creating custom {@link Check} instances.
 * Used with {@code Reality.checkThat(actual, MyCheck::new)} for zero-boilerplate extension.
 *
 * @param <C> the concrete check type
 * @param <T> the type of the value under test
 */
@FunctionalInterface
public interface CheckFactory<C extends Check<C, T>, T> {

    C create(T actual, FailureHandler failureHandler);
}
