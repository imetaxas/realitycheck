package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class SuppressedExceptionCheckTest {

    @Test
    void hasSuppressed_passes() {
        var ex = new RuntimeException("main");
        ex.addSuppressed(new IOException("cleanup failed"));

        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; }).hasSuppressed());
    }

    @Test
    void hasSuppressed_failsWhenNone() {
        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw new RuntimeException("no suppressed"); })
                        .hasSuppressed());
    }

    @Test
    void hasNoSuppressed_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("clean"); })
                        .hasNoSuppressed());
    }

    @Test
    void hasNoSuppressed_failsWhenPresent() {
        var ex = new RuntimeException("main");
        ex.addSuppressed(new IOException("oops"));

        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; }).hasNoSuppressed());
    }

    @Test
    void hasSuppressedCount_passes() {
        var ex = new RuntimeException("main");
        ex.addSuppressed(new IOException("one"));
        ex.addSuppressed(new IllegalStateException("two"));

        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; }).hasSuppressedCount(2));
    }

    @Test
    void hasSuppressedCount_fails() {
        var ex = new RuntimeException("main");
        ex.addSuppressed(new IOException("one"));

        var e = assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; }).hasSuppressedCount(3));
        assertTrue(e.getMessage().contains("3"));
    }

    @Test
    void hasSuppressedInstanceOf_passes() {
        var ex = new RuntimeException("main");
        ex.addSuppressed(new IOException("io problem"));
        ex.addSuppressed(new IllegalStateException("state problem"));

        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; })
                        .hasSuppressedInstanceOf(IOException.class));
    }

    @Test
    void hasSuppressedInstanceOf_fails() {
        var ex = new RuntimeException("main");
        ex.addSuppressed(new IOException("io"));

        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; })
                        .hasSuppressedInstanceOf(IllegalStateException.class));
    }

    @Test
    void suppressedException_extractsForFurtherAssertions() {
        var ex = new RuntimeException("main");
        ex.addSuppressed(new IOException("first suppressed"));
        ex.addSuppressed(new IllegalStateException("second suppressed"));

        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; })
                        .suppressedException(0)
                        .isInstanceOf(IOException.class)
                        .hasMessage("first suppressed"));
    }

    @Test
    void suppressedException_outOfBounds() {
        var ex = new RuntimeException("main");
        ex.addSuppressed(new IOException("only one"));

        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; }).suppressedException(5));
    }
}
