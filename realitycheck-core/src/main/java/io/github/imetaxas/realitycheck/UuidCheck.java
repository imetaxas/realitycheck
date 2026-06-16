package io.github.imetaxas.realitycheck;

import java.util.UUID;

/**
 * Fluent assertions for {@link UUID} values.
 *
 * <pre>{@code
 * checkThat(id).isNotNil().isVersion4();
 * checkThat(traceId).hasVersion(4).toString().hasLength(36);
 * }</pre>
 */
public final class UuidCheck extends AbstractCheck<UuidCheck, UUID> {

    private static final UUID NIL = new UUID(0L, 0L);

    UuidCheck(UUID actual, FailureHandler handler) {
        super(actual, handler);
    }

    public UuidCheck isNil() {
        return failureHandler().check(self(), actual().equals(NIL),
                "expected nil UUID but was: <%s>", actual());
    }

    public UuidCheck isNotNil() {
        return failureHandler().check(self(), !actual().equals(NIL),
                "expected a non-nil UUID");
    }

    public UuidCheck hasVersion(int expected) {
        return failureHandler().check(self(), actual().version() == expected,
                "expected UUID version <%d> but was: <%d>",
                expected, actual().version());
    }

    public UuidCheck isVersion4() {
        return hasVersion(4);
    }

    public UuidCheck hasVariant(int expected) {
        return failureHandler().check(self(), actual().variant() == expected,
                "expected UUID variant <%d> but was: <%d>",
                expected, actual().variant());
    }

    /** Extracts the string representation for further assertions. */
    public StringCheck asString() {
        return new StringCheck(actual().toString(), failureHandler());
    }
}
