package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.junit.jupiter.api.Test;

class InputStreamCheckTest {

    private static ByteArrayInputStream streamOf(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    @Test
    void hasSameContentAs_passes() {
        byte[] data = {1, 2, 3};
        assertDoesNotThrow(() ->
                checkThat(streamOf(data)).hasSameContentAs(streamOf(data.clone())));
    }

    @Test
    void hasSameContentAs_fails() {
        assertThrows(AssertionError.class,
                () -> checkThat(streamOf(new byte[]{1, 2})).hasSameContentAs(streamOf(new byte[]{1, 2, 3})));
    }

    @Test
    void hasNotSameContentAs_passes() {
        assertDoesNotThrow(() ->
                checkThat(streamOf(new byte[]{1})).hasNotSameContentAs(streamOf(new byte[]{2})));
    }

    @Test
    void hasNotSameContentAs_fails_when_identical() {
        byte[] data = {9, 9};
        assertThrows(AssertionError.class,
                () -> checkThat(streamOf(data)).hasNotSameContentAs(streamOf(data.clone())));
    }

    @Test
    void hasSize_passes() {
        assertDoesNotThrow(() -> checkThat(streamOf(new byte[]{0, 0, 0})).hasSize(3));
    }

    @Test
    void hasSize_fails() {
        assertThrows(AssertionError.class, () -> checkThat(streamOf(new byte[]{1})).hasSize(10));
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(streamOf(new byte[]{0})).isNotEmpty());
    }

    @Test
    void isNotEmpty_fails() {
        assertThrows(AssertionError.class, () -> checkThat(streamOf(new byte[]{})).isNotEmpty());
    }

    @Test
    void readAll_throwsUncheckedIOException() {
        InputStream broken = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("simulated");
            }
        };
        assertThrows(UncheckedIOException.class,
                () -> checkThat(broken).isNotEmpty());
    }
}
