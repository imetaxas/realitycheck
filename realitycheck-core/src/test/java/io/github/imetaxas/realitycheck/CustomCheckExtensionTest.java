package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Demonstrates the zero-boilerplate custom check extension model.
 */
class CustomCheckExtensionTest {

    record Email(String address) {}

    /**
     * Custom check implemented as a record — 3 lines of overhead.
     */
    record EmailCheck(Email actual, FailureHandler failureHandler)
            implements Check<EmailCheck, Email> {

        @Override
        public EmailCheck self() {
            return this;
        }

        public EmailCheck hasValidFormat() {
            return failureHandler.check(self(),
                    actual.address() != null && actual.address().contains("@"),
                    "expected valid email format but was: <%s>", actual.address());
        }

        public EmailCheck hasDomain(String domain) {
            return failureHandler.check(self(),
                    actual.address() != null && actual.address().endsWith("@" + domain),
                    "expected domain <%s> but was: <%s>", domain, actual.address());
        }
    }

    @Test
    void customCheck_passes() {
        assertDoesNotThrow(() ->
                checkThat(new Email("user@example.com"), EmailCheck::new)
                        .isNotNull()
                        .hasValidFormat()
                        .hasDomain("example.com"));
    }

    @Test
    void customCheck_failsWithMessage() {
        var e = assertThrows(AssertionError.class, () ->
                checkThat(new Email("invalid"), EmailCheck::new).hasValidFormat());
        assertTrue(e.getMessage().contains("invalid"));
    }

    @Test
    void customCheck_worksWithSoftChecks() {
        var e = assertThrows(AssertionError.class, () -> Reality.checkAll(softly -> {
            softly.checkThat(new Email("bad"), EmailCheck::new).hasValidFormat();
            softly.checkThat(new Email("also-bad"), EmailCheck::new).hasValidFormat();
        }));
        assertTrue(e.getMessage().contains("Multiple failures (2)"));
    }
}
