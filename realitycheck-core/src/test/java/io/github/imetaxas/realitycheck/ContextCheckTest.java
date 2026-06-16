package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkWithContext;
import static io.github.imetaxas.realitycheck.Reality.checkWithMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class ContextCheckTest {

    @Test
    void checkWithContext_appendsToDefaultMessage() {
        var e = assertThrows(AssertionError.class, () ->
                checkWithContext("validating user registration").that("").isNotEmpty());

        assertTrue(e.getMessage().contains("expected a non-empty string"));
        assertTrue(e.getMessage().contains("[context: validating user registration]"));
    }

    @Test
    void checkWithContext_passesNormally() {
        assertDoesNotThrow(() ->
                checkWithContext("validating user registration").that("Alice").isNotEmpty());
    }

    @Test
    void checkWithContext_worksWithNumbers() {
        var e = assertThrows(AssertionError.class, () ->
                checkWithContext("age validation").that(-1).isPositive());

        assertTrue(e.getMessage().contains("-1"));
        assertTrue(e.getMessage().contains("[context: age validation]"));
    }

    @Test
    void checkWithContext_worksWithBooleans() {
        var e = assertThrows(AssertionError.class, () ->
                checkWithContext("feature flag check").that(false).isTrue());

        assertTrue(e.getMessage().contains("[context: feature flag check]"));
    }

    @Test
    void checkWithMessage_replaces_notAppends() {
        var e = assertThrows(AssertionError.class, () ->
                checkWithMessage("User name is required").that("").isNotEmpty());

        assertTrue(e.getMessage().startsWith("User name is required"));
        assertTrue(e.getMessage().contains("detail:"));
        assertFalse(e.getMessage().contains("[context:"));
    }

    @Test
    void checkWithContext_vs_checkWithMessage_comparison() {
        var contextError = assertThrows(AssertionError.class, () ->
                checkWithContext("signup form").that("").isNotEmpty());

        var messageError = assertThrows(AssertionError.class, () ->
                checkWithMessage("Name is required").that("").isNotEmpty());

        // Context appends: "expected a non-empty string [context: signup form]"
        assertTrue(contextError.getMessage().startsWith("expected"));
        assertTrue(contextError.getMessage().endsWith("]"));

        // Message replaces: "Name is required\n  detail: expected a non-empty string"
        assertTrue(messageError.getMessage().startsWith("Name is required"));
    }
}
