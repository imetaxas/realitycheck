package io.github.imetaxas.realitycheck;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;

/**
 * Fluent assertions for {@link InputStream} values.
 */
public final class InputStreamCheck extends AbstractCheck<InputStreamCheck, InputStream> {

    private byte[] cached;

    InputStreamCheck(InputStream actual, FailureHandler handler) {
        super(actual, handler);
    }

    public InputStreamCheck hasSameContentAs(InputStream expected) {
        byte[] expectedBytes = readFully(expected);
        if (!Arrays.equals(bytes(), expectedBytes)) {
            failureHandler().fail("input stream contents differ (actual: %d bytes, expected: %d bytes)",
                    bytes().length, expectedBytes.length);
        }
        return self();
    }

    public InputStreamCheck hasNotSameContentAs(InputStream expected) {
        byte[] expectedBytes = readFully(expected);
        if (Arrays.equals(bytes(), expectedBytes)) {
            failureHandler().fail("expected different input stream content but they are identical (%d bytes)",
                    bytes().length);
        }
        return self();
    }

    public InputStreamCheck hasSize(int expectedBytes) {
        if (bytes().length != expectedBytes) {
            failureHandler().fail("expected stream size <%d> bytes but was: <%d> bytes",
                    expectedBytes, bytes().length);
        }
        return self();
    }

    public InputStreamCheck isNotEmpty() {
        if (bytes().length == 0) {
            failureHandler().fail("expected non-empty input stream");
        }
        return self();
    }

    /**
     * Reads and caches the actual stream content. The stream is consumed once
     * and the bytes are reused for all subsequent assertions, enabling fluent chaining.
     */
    private byte[] bytes() {
        if (cached == null) {
            cached = readFully(actual());
        }
        return cached;
    }

    private static byte[] readFully(InputStream is) {
        try {
            var out = new ByteArrayOutputStream();
            is.transferTo(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
