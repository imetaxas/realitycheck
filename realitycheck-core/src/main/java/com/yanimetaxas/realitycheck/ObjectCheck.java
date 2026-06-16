package com.yanimetaxas.realitycheck;

/**
 * Fluent assertions for arbitrary objects.
 *
 * @param <T> the type of the value under test
 */
public final class ObjectCheck<T> extends AbstractCheck<ObjectCheck<T>, T> {

    ObjectCheck(T actual, FailureHandler handler) {
        super(actual, handler);
    }

    public ObjectCheck<T> hasToString(String expected) {
        String str = String.valueOf(actual());
        return failureHandler().check(self(), str.equals(expected),
                "expected toString() <%s> but was: <%s>", expected, str);
    }

    public ObjectCheck<T> hasSameHashCodeAs(Object other) {
        int actualHash = actual() == null ? 0 : actual().hashCode();
        int otherHash = other == null ? 0 : other.hashCode();
        return failureHandler().check(self(), actualHash == otherHash,
                "expected hashCode <%d> but was: <%d>", otherHash, actualHash);
    }
}
