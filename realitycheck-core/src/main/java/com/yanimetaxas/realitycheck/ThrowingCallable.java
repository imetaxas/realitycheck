package com.yanimetaxas.realitycheck;

/**
 * A callable that may throw any {@link Throwable}.
 * Used with {@code Reality.checkThatThrownBy(...)}.
 */
@FunctionalInterface
public interface ThrowingCallable {

    void call() throws Throwable;
}
