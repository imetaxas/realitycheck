package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static io.github.imetaxas.realitycheck.Reality.checkWithMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BooleanCheckTest {

    @Test
    void isTrue_passes() {
        assertDoesNotThrow(() -> checkThat(true).isTrue());
    }

    @Test
    void isTrue_fails() {
        assertThrows(AssertionError.class, () -> checkThat(false).isTrue());
    }

    @Test
    void isFalse_passes() {
        assertDoesNotThrow(() -> checkThat(false).isFalse());
    }

    @Test
    void isFalse_fails() {
        assertThrows(AssertionError.class, () -> checkThat(true).isFalse());
    }

    @Test
    void customMessage_works() {
        var e = assertThrows(AssertionError.class,
                () -> checkWithMessage("Feature flag should be on").that(false).isTrue());
        assertTrue(e.getMessage().contains("Feature flag should be on"));
    }

    @Test
    void isTrue_failsOnFalse() {
        assertThrows(AssertionError.class, () -> checkThat(false).isTrue());
    }

    @Test
    void isFalse_failsOnTrue() {
        assertThrows(AssertionError.class, () -> checkThat(true).isFalse());
    }
}
