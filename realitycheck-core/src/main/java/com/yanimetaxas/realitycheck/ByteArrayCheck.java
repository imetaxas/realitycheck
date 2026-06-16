package com.yanimetaxas.realitycheck;

import java.util.Arrays;
import java.util.Base64;

/**
 * Fluent assertions for {@code byte[]} arrays.
 *
 * <pre>{@code
 * checkThat(data).isNotEmpty().hasLength(256);
 * checkThat(header).startsWith(new byte[]{0x50, 0x4B}); // ZIP magic bytes
 * checkThat(payload).toBase64().startsWith("eyJ");
 * }</pre>
 */
public final class ByteArrayCheck extends AbstractCheck<ByteArrayCheck, byte[]> {

    ByteArrayCheck(byte[] actual, FailureHandler handler) {
        super(actual, handler);
    }

    public ByteArrayCheck isEmpty() {
        return failureHandler().check(self(), actual().length == 0,
                "expected an empty byte[] but had <%d> bytes", actual().length);
    }

    public ByteArrayCheck isNotEmpty() {
        return failureHandler().check(self(), actual().length != 0,
                "expected a non-empty byte[]");
    }

    public ByteArrayCheck hasLength(int expected) {
        return failureHandler().check(self(), actual().length == expected,
                "expected byte[] length <%d> but was: <%d>",
                expected, actual().length);
    }

    public ByteArrayCheck hasLengthBetween(int minInclusive, int maxInclusive) {
        int len = actual().length;
        return failureHandler().check(self(),
                len >= minInclusive && len <= maxInclusive,
                "expected byte[] length between <%d> and <%d> but was: <%d>",
                minInclusive, maxInclusive, len);
    }

    public ByteArrayCheck startsWith(byte[] prefix) {
        if (actual().length < prefix.length) {
            failureHandler().fail("expected byte[] to start with %s but array is shorter (%d < %d)",
                    toHexString(prefix), actual().length, prefix.length);
        } else {
            for (int i = 0; i < prefix.length; i++) {
                if (actual()[i] != prefix[i]) {
                    failureHandler().fail("expected byte[] to start with %s but differs at index <%d>",
                            toHexString(prefix), i);
                }
            }
        }
        return self();
    }

    public ByteArrayCheck containsExactly(byte... expected) {
        return failureHandler().check(self(), Arrays.equals(actual(), expected),
                "expected exactly %s but was: %s",
                toHexString(expected), toHexString(actual()));
    }

    public ByteArrayCheck contains(byte value) {
        boolean found = false;
        for (byte b : actual()) {
            if (b == value) { found = true; break; }
        }
        if (!found) {
            failureHandler().fail("expected byte[] to contain <0x%02X> but it did not",
                    value);
        }
        return self();
    }

    /** Extracts the hex representation for further assertions. */
    public StringCheck toHex() {
        return new StringCheck(toHexString(actual()), failureHandler());
    }

    /** Extracts the Base64 representation for further assertions. */
    public StringCheck toBase64() {
        return new StringCheck(Base64.getEncoder().encodeToString(actual()), failureHandler());
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(String.format("0x%02X", bytes[i]));
            if (i >= 15 && bytes.length > 16) {
                sb.append(", ... (").append(bytes.length).append(" bytes total)");
                break;
            }
        }
        return sb.append("]").toString();
    }
}
