package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UuidCheckTest {

    @Test
    void isNotNil_passes() {
        assertDoesNotThrow(() -> checkThat(UUID.randomUUID()).isNotNil());
    }

    @Test
    void isNil_passes() {
        assertDoesNotThrow(() -> checkThat(new UUID(0L, 0L)).isNil());
    }

    @Test
    void isNil_fails() {
        assertThrows(AssertionError.class, () -> checkThat(UUID.randomUUID()).isNil());
    }

    @Test
    void isVersion4_passes() {
        assertDoesNotThrow(() -> checkThat(UUID.randomUUID()).isVersion4());
    }

    @Test
    void hasVersion_passes() {
        assertDoesNotThrow(() -> checkThat(UUID.randomUUID()).hasVersion(4));
    }

    @Test
    void hasVersion_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat(UUID.randomUUID()).hasVersion(1));
    }

    @Test
    void hasVariant_passes() {
        UUID uuid = UUID.randomUUID();
        assertDoesNotThrow(() -> checkThat(uuid).hasVariant(uuid.variant()));
    }

    @Test
    void asString_extractor() {
        UUID uuid = UUID.randomUUID();
        assertDoesNotThrow(() ->
                checkThat(uuid).asString().hasLength(36).contains("-"));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThat(UUID.randomUUID()).isNotNil().isVersion4());
    }

    @Test
    void isVersion4_pass() {
        assertDoesNotThrow(() -> checkThat(UUID.randomUUID()).isVersion4());
    }

    @Test
    void isVersion4_fail() {
        UUID v3 = UUID.nameUUIDFromBytes("test".getBytes());
        assertThrows(AssertionError.class, () -> checkThat(v3).isVersion4());
    }

    @Test
    void hasVersion_pass() {
        assertDoesNotThrow(() -> checkThat(UUID.randomUUID()).hasVersion(4));
    }

    @Test
    void hasVersion_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(UUID.randomUUID()).hasVersion(1));
    }

    @Test
    void isNil_pass() {
        assertDoesNotThrow(() -> checkThat(new UUID(0L, 0L)).isNil());
    }

    @Test
    void isNil_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(UUID.randomUUID()).isNil());
    }

    @Test
    void isNotNil_pass() {
        assertDoesNotThrow(() -> checkThat(UUID.randomUUID()).isNotNil());
    }

    @Test
    void isNotNil_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(new UUID(0L, 0L)).isNotNil());
    }

    @Test
    void hasVariant_pass() {
        UUID uuid = UUID.randomUUID();
        assertDoesNotThrow(() -> checkThat(uuid).hasVariant(uuid.variant()));
    }

    @Test
    void hasVariant_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(UUID.randomUUID()).hasVariant(99));
    }

    @Test
    void isNil_failsOnNonNil() {
        assertThrows(AssertionError.class,
                () -> checkThat(UUID.randomUUID()).isNil());
    }

    @Test
    void isNotNil_failsOnNil() {
        assertThrows(AssertionError.class,
                () -> checkThat(new UUID(0L, 0L)).isNotNil());
    }

    @Test
    void hasVersion_failsOnWrongVersion() {
        assertThrows(AssertionError.class,
                () -> checkThat(UUID.randomUUID()).hasVersion(1));
    }

    @Test
    void hasVariant_failsOnWrongVariant() {
        assertThrows(AssertionError.class,
                () -> checkThat(UUID.randomUUID()).hasVariant(99));
    }

    @Test
    void asString_extractorChains() {
        assertDoesNotThrow(
                () -> checkThat(UUID.randomUUID()).asString().hasLength(36));
    }

    @Test
    void isVersion4_failsOnNonV4() {
        UUID v3 = UUID.nameUUIDFromBytes("test".getBytes());
        assertThrows(AssertionError.class, () -> checkThat(v3).isVersion4());
    }
}
