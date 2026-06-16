package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ByteArrayCheckTest {

    @Test
    void hasLength_passes() {
        assertDoesNotThrow(() -> checkThat(new byte[]{1, 2, 3}).hasLength(3));
    }

    @Test
    void isEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(new byte[]{}).isEmpty());
    }

    @Test
    void isNotEmpty_passes() {
        assertDoesNotThrow(() -> checkThat(new byte[]{1}).isNotEmpty());
    }

    @Test
    void hasLengthBetween_passes() {
        assertDoesNotThrow(() ->
                checkThat(new byte[]{1, 2, 3}).hasLengthBetween(1, 5));
    }

    @Test
    void hasLengthBetween_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(new byte[]{1}).hasLengthBetween(5, 10));
    }

    @Test
    void startsWith_passes() {
        assertDoesNotThrow(() ->
                checkThat(new byte[]{0x50, 0x4B, 0x03, 0x04})
                        .startsWith(new byte[]{0x50, 0x4B}));
    }

    @Test
    void startsWith_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(new byte[]{0x00, 0x01}).startsWith(new byte[]{0x50, 0x4B}));
    }

    @Test
    void containsExactly_passes() {
        assertDoesNotThrow(() ->
                checkThat(new byte[]{1, 2, 3}).containsExactly((byte) 1, (byte) 2, (byte) 3));
    }

    @Test
    void contains_passes() {
        assertDoesNotThrow(() -> checkThat(new byte[]{10, 20, 30}).contains((byte) 20));
    }

    @Test
    void contains_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(new byte[]{1, 2}).contains((byte) 99));
    }

    @Test
    void toHex_extractor() {
        assertDoesNotThrow(() ->
                checkThat(new byte[]{(byte) 0xCA, (byte) 0xFE}).toHex().contains("0xCA"));
    }

    @Test
    void toBase64_extractor() {
        assertDoesNotThrow(() ->
                checkThat("hello".getBytes()).toBase64().isEqualTo("aGVsbG8="));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat(new byte[]{1, 2, 3}).isNotEmpty().hasLength(3).contains((byte) 2));
    }

    @Test
    void isEmpty_pass() {
        assertDoesNotThrow(() -> checkThat(new byte[]{}).isEmpty());
    }

    @Test
    void isEmpty_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new byte[]{1, 2, 3}).isEmpty());
    }

    @Test
    void isNotEmpty_pass() {
        assertDoesNotThrow(() -> checkThat(new byte[]{1}).isNotEmpty());
    }

    @Test
    void isNotEmpty_fail() {
        assertThrows(AssertionError.class, () -> checkThat(new byte[]{}).isNotEmpty());
    }

    @Test
    void startsWith_pass() {
        assertDoesNotThrow(() ->
                checkThat(new byte[]{0x50, 0x4B, 0x03}).startsWith(new byte[]{0x50, 0x4B}));
    }

    @Test
    void startsWith_fail_longerPrefix() {
        assertThrows(AssertionError.class, () ->
                checkThat(new byte[]{0x50}).startsWith(new byte[]{0x50, 0x4B, 0x03}));
    }

    @Test
    void startsWith_fail_wrongByte() {
        assertThrows(AssertionError.class, () ->
                checkThat(new byte[]{0x50, 0x00}).startsWith(new byte[]{0x50, 0x4B}));
    }

    @Test
    void containsExactly_pass() {
        assertDoesNotThrow(() ->
                checkThat(new byte[]{1, 2}).containsExactly((byte) 1, (byte) 2));
    }

    @Test
    void containsExactly_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new byte[]{1, 2}).containsExactly((byte) 1, (byte) 3));
    }

    @Test
    void contains_pass() {
        assertDoesNotThrow(() -> checkThat(new byte[]{1, 2, 3}).contains((byte) 2));
    }

    @Test
    void contains_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new byte[]{1, 2, 3}).contains((byte) 9));
    }

    @Test
    void hasLength_pass() {
        assertDoesNotThrow(() -> checkThat(new byte[]{1, 2}).hasLength(2));
    }

    @Test
    void hasLength_fail() {
        assertThrows(AssertionError.class, () -> checkThat(new byte[]{1, 2}).hasLength(5));
    }

    @Test
    void hasLengthBetween_pass() {
        assertDoesNotThrow(() -> checkThat(new byte[]{1, 2, 3}).hasLengthBetween(2, 5));
    }

    @Test
    void hasLengthBetween_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new byte[]{1}).hasLengthBetween(5, 10));
    }

    @Test
    void containsExactly_failsOnMismatch() {
        assertThrows(AssertionError.class,
                () -> checkThat(new byte[]{1, 2}).containsExactly((byte) 1, (byte) 3));
    }

    @Test
    void startsWith_failsOnWrongPrefix() {
        assertThrows(AssertionError.class,
                () -> checkThat(new byte[]{0x01, 0x02}).startsWith(new byte[]{0x50, 0x4B}));
    }

    @Test
    void startsWith_failsWhenTooShort() {
        assertThrows(AssertionError.class,
                () -> checkThat(new byte[]{0x01}).startsWith(new byte[]{0x01, 0x02, 0x03}));
    }

    @Test
    void hasLengthBetween_failsOutsideRange() {
        assertThrows(AssertionError.class,
                () -> checkThat(new byte[]{1}).hasLengthBetween(5, 10));
    }

    @Test
    void contains_failsWhenNotFound() {
        assertThrows(AssertionError.class,
                () -> checkThat(new byte[]{1, 2, 3}).contains((byte) 99));
    }

    @Test
    void isEmpty_failsWhenNotEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThat(new byte[]{1}).isEmpty());
    }

    @Test
    void isNotEmpty_failsWhenEmpty() {
        assertThrows(AssertionError.class,
                () -> checkThat(new byte[]{}).isNotEmpty());
    }

    @Test
    void hasLength_failsOnWrongLength() {
        assertThrows(AssertionError.class,
                () -> checkThat(new byte[]{1, 2}).hasLength(5));
    }

    @Test
    void toHex_truncates_long_array() {
        byte[] big = new byte[20];
        for (int i = 0; i < big.length; i++) big[i] = (byte) i;
        StringCheck hex = checkThat(big).toHex();
        assertDoesNotThrow(() -> hex.contains("..."));
    }

    @Test
    void startsWith_tooShort() {
        assertThrows(AssertionError.class,
                () -> checkThat(new byte[]{0x01}).startsWith(new byte[]{0x01, 0x02, 0x03}));
    }

    @Test
    void toHex_passes() {
        assertDoesNotThrow(() -> checkThat(new byte[]{(byte) 0xCA, (byte) 0xFE}).toHex().contains("0xCA"));
    }

    @Test
    void toBase64_passes() {
        assertDoesNotThrow(() -> checkThat(new byte[]{1, 2, 3}).toBase64().isNotEmpty());
    }

    @Test
    void containsByte_passes() {
        assertDoesNotThrow(() -> checkThat(new byte[]{1, 2, 3}).contains((byte) 2));
    }

    @Test
    void containsByte_fails() {
        assertThrows(AssertionError.class, () -> checkThat(new byte[]{1, 2, 3}).contains((byte) 99));
    }

    @Test
    void hasLength_fails() {
        assertThrows(AssertionError.class, () -> checkThat(new byte[]{1, 2}).hasLength(5));
    }

    @Test
    void isNotEmpty_fails() {
        assertThrows(AssertionError.class, () -> checkThat(new byte[]{}).isNotEmpty());
    }
}
