package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ThrowableCheckTest {

    @Nested
    class NullMessage {
        // hasMessageStartingWith / hasMessageMatching with `msg != null && ...`
        // when msg IS null → the short-circuit false branch of && is taken.

        @Test
        void hasMessageStartingWith_nullMessage_fails() {
            assertThrows(AssertionError.class,
                    () -> checkThatThrownBy(this::throwNpe).hasMessageStartingWith("prefix"));
        }

        @Test
        void hasMessageMatching_nullMessage_fails() {
            assertThrows(AssertionError.class,
                    () -> checkThatThrownBy(this::throwNpe).hasMessageMatching(".*"));
        }

        private void throwNpe() { throw new NullPointerException(); }
    }

    @Nested
    class SuppressedExceptions {

        @Test
        void suppressedException_validIndex_returnsThrowableCheck() {
            var outer = new RuntimeException("outer");
            outer.addSuppressed(new IllegalStateException("suppressed"));
            assertDoesNotThrow(() ->
                    checkThatThrownBy(() -> { throw outer; })
                            .suppressedException(0)
                            .isInstanceOf(IllegalStateException.class));
        }

        @Test
        void suppressedException_negativeIndex_softMode_returnsNullCheck() {
            var handler = new SoftFailureHandler();
            var outer = new RuntimeException("outer");
            outer.addSuppressed(new IllegalStateException("s"));
            ThrowableCheck check = CheckFacade.thrownBy(() -> { throw outer; }, handler);
            ThrowableCheck result = check.suppressedException(-1);
            assertNull(result.actual());
            assertEquals(1, handler.failures().size());
        }

        @Test
        void rootCauseOf_noCause_returnsSelf() {
            var lone = new RuntimeException("lone");
            assertDoesNotThrow(() ->
                    checkThatThrownBy(() -> { throw lone; })
                            .hasRootCauseInstanceOf(RuntimeException.class));
        }
    }

    @Nested
    class WhenActualIsNull {
        // Use a soft handler so thrownBy() records a failure instead of throwing,
        // giving us a ThrowableCheck with null actual to exercise all null guards.

        private ThrowableCheck nullCheck() {
            return CheckFacade.thrownBy(() -> {}, new SoftFailureHandler());
        }

        @Test
        void allAssertions_silentlyReturn_whenActualIsNull() {
            assertDoesNotThrow(() ->
                nullCheck()
                     .isExactlyInstanceOf(Exception.class)
                     .hasMessageStartingWith("x")
                     .hasMessageMatching(".*")
                     .hasCause()
                     .hasCauseInstanceOf(Exception.class)
                     .hasRootCauseInstanceOf(Exception.class)
                     .hasSuppressed()
                     .hasNoSuppressed()
                     .hasSuppressedCount(0)
                     .hasSuppressedInstanceOf(Exception.class));
        }

        @Test
        void extractorMethods_silentlyReturn_whenActualIsNull() {
            assertDoesNotThrow(() -> {
                ThrowableCheck check = nullCheck();
                assertNull(check.cause().actual());
                assertNull(check.rootCause().actual());
                assertNull(check.message().actual());
                assertNull(check.suppressedException(0).actual());
            });
        }
    }

    @Test
    void isInstanceOf_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new IllegalArgumentException("bad"); })
                        .isInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void isInstanceOf_matchesSupertype() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new IllegalArgumentException("bad"); })
                        .isInstanceOf(RuntimeException.class));
    }

    @Test
    void isInstanceOf_failsOnWrongType() {
        var e = assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw new IllegalArgumentException("bad"); })
                        .isInstanceOf(IOException.class));
        assertTrue(e.getMessage().contains("IOException"));
        assertTrue(e.getMessage().contains("IllegalArgumentException"));
    }

    @Test
    void isExactlyInstanceOf_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new IllegalArgumentException("bad"); })
                        .isExactlyInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void hasMessage_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("oops"); })
                        .hasMessage("oops"));
    }

    @Test
    void hasMessageContaining_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("file not found: /tmp/x"); })
                        .hasMessageContaining("not found"));
    }

    @Test
    void hasMessageStartingWith_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("Connection refused"); })
                        .hasMessageStartingWith("Connection"));
    }

    @Test
    void hasMessageMatching_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("Error code: 42"); })
                        .hasMessageMatching("Error code: \\d+"));
    }

    @Test
    void hasNoCause_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("no cause"); })
                        .hasNoCause());
    }

    @Test
    void hasCause_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("wrap", new IOException("io")); })
                        .hasCause());
    }

    @Test
    void hasCauseInstanceOf_passes() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("wrap", new IOException("io")); })
                        .hasCauseInstanceOf(IOException.class));
    }

    @Test
    void cause_extractsForFurtherAssertions() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("wrap", new IOException("disk full")); })
                        .cause()
                        .isInstanceOf(IOException.class)
                        .hasMessageContaining("disk full"));
    }

    @Test
    void rootCause_traversesChain() {
        var root = new IllegalStateException("root problem");
        var mid = new IOException("mid", root);
        var top = new RuntimeException("top", mid);

        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw top; })
                        .rootCause()
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("root problem"));
    }

    @Test
    void hasRootCauseInstanceOf_passes() {
        var root = new IllegalStateException("root");
        var top = new RuntimeException("top", new IOException("mid", root));

        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw top; })
                        .hasRootCauseInstanceOf(IllegalStateException.class));
    }

    @Test
    void message_extractsAsStringCheck() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException("Error: code 42"); })
                        .message()
                        .startsWith("Error")
                        .contains("42")
                        .hasLength(14));
    }

    @Test
    void nothingThrown_failsImmediately() {
        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { /* no exception */ }));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new IllegalArgumentException("must not be null"); })
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("must not be null")
                        .hasNoCause());
    }

    @Test
    void rootCause_noCause_returnsSelf() {
        RuntimeException ex = new RuntimeException("top");
        ThrowableCheck check = checkThatThrownBy(() -> { throw ex; });
        assertDoesNotThrow(() -> check.rootCause().hasMessage("top"));
    }

    @Test
    void hasCauseInstanceOf_pass() {
        RuntimeException ex = new RuntimeException("top", new IllegalArgumentException("cause"));
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; })
                        .hasCauseInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void hasCauseInstanceOf_wrongType() {
        RuntimeException ex = new RuntimeException("top", new IllegalStateException("cause"));
        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; })
                        .hasCauseInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void hasCauseInstanceOf_noCause() {
        RuntimeException ex = new RuntimeException("no cause");
        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; })
                        .hasCauseInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void hasNoCause_pass() {
        RuntimeException ex = new RuntimeException("top");
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; }).hasNoCause());
    }

    @Test
    void hasNoCause_fail() {
        RuntimeException ex = new RuntimeException("top", new Exception("cause"));
        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; }).hasNoCause());
    }

    @Test
    void cause_extractor_pass() {
        RuntimeException ex = new RuntimeException("top", new IllegalStateException("inner"));
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; })
                        .cause().hasMessage("inner"));
    }

    @Test
    void cause_extractor_noCause() {
        RuntimeException ex = new RuntimeException("top");
        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; }).cause());
    }

    @Test
    void hasRootCauseInstanceOf_pass() {
        RuntimeException ex = new RuntimeException("top",
                new IllegalStateException("mid", new IllegalArgumentException("root")));
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; })
                        .hasRootCauseInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void hasRootCauseInstanceOf_fail() {
        RuntimeException ex = new RuntimeException("top",
                new IllegalStateException("root"));
        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; })
                        .hasRootCauseInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void message_extractor() {
        RuntimeException ex = new RuntimeException("hello world");
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; }).message().contains("world"));
    }

    @Test
    void hasMessageStartingWith_pass() {
        RuntimeException ex = new RuntimeException("hello world");
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; }).hasMessageStartingWith("hello"));
    }

    @Test
    void hasMessageStartingWith_fail() {
        RuntimeException ex = new RuntimeException("hello world");
        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; }).hasMessageStartingWith("world"));
    }

    @Test
    void hasMessageMatching_pass() {
        RuntimeException ex = new RuntimeException("error code 42");
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw ex; }).hasMessageMatching(".*\\d+"));
    }

    @Test
    void hasMessageMatching_fail() {
        RuntimeException ex = new RuntimeException("no digits here");
        assertThrows(AssertionError.class, () ->
                checkThatThrownBy(() -> { throw ex; }).hasMessageMatching("^\\d+$"));
    }

    @Test
    void hasSuppressed_failsWhenNoSuppressed() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("solo"); })
                        .hasSuppressed());
    }

    @Test
    void hasNoSuppressed_failsWhenSuppressedPresent() {
        var ex = new RuntimeException("top");
        ex.addSuppressed(new IllegalStateException("suppressed"));
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw ex; }).hasNoSuppressed());
    }

    @Test
    void hasSuppressedCount_failsOnWrongCount() {
        var ex = new RuntimeException("top");
        ex.addSuppressed(new IllegalStateException("s1"));
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw ex; }).hasSuppressedCount(5));
    }

    @Test
    void hasSuppressedInstanceOf_failsWhenNoMatchingType() {
        var ex = new RuntimeException("top");
        ex.addSuppressed(new IllegalStateException("s1"));
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw ex; })
                        .hasSuppressedInstanceOf(IOException.class));
    }

    @Test
    void suppressedException_failsOnOutOfRangeIndex() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("solo"); })
                        .suppressedException(0));
    }

    @Test
    void isExactlyInstanceOf_failsOnWrongType() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("x"); })
                        .isExactlyInstanceOf(IllegalArgumentException.class));
    }

    @Test
    void rootCause_chainsToDeepestCause() {
        var root = new IllegalArgumentException("root");
        var mid = new IOException("mid", root);
        var top = new RuntimeException("top", mid);
        assertDoesNotThrow(
                () -> checkThatThrownBy(() -> { throw top; })
                        .rootCause().hasMessage("root"));
    }

    @Test
    void hasRootCauseInstanceOf_failsOnWrongType() {
        var root = new IllegalArgumentException("root");
        var top = new RuntimeException("top", root);
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw top; })
                        .hasRootCauseInstanceOf(IOException.class));
    }

    @Test
    void hasMessageContaining_failsWhenSubstringMissing() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("abc"); })
                        .hasMessageContaining("zzz"));
    }

    @Test
    void hasMessageStartingWith_failsOnWrongPrefix() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("hello world"); })
                        .hasMessageStartingWith("world"));
    }

    @Test
    void hasMessageMatching_failsOnNoMatch() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("abc"); })
                        .hasMessageMatching("^\\d+$"));
    }

    @Test
    void message_extractorChainsToStringCheck() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("hello"); })
                        .message().isEqualTo("goodbye"));
    }

    @Test
    void cause_extractorChainsToThrowableCheck() {
        var ex = new RuntimeException("top", new IOException("inner"));
        assertDoesNotThrow(
                () -> checkThatThrownBy(() -> { throw ex; })
                        .cause().hasMessage("inner"));
    }

    @Test
    void cause_failsWhenNoCause() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("solo"); }).cause());
    }

    @Test
    void hasMessage_failsWhenDifferent() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("a"); }).hasMessage("b"));
    }

    @Test
    void hasMessageContaining_failsWhenMessageNull() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException(); }).hasMessageContaining("x"));
    }

    @Test
    void hasNullMessage_passes_whenExceptionHasNoMessage() {
        assertDoesNotThrow(() ->
                checkThatThrownBy(() -> { throw new RuntimeException(); })
                        .hasNullMessage());
    }

    @Test
    void hasNullMessage_failsWhenMessagePresent() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("x"); }).hasNullMessage());
    }

    @Test
    void hasCause_failsWhenNone() {
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw new RuntimeException("solo"); }).hasCause());
    }

    @Test
    void rootCause_messageAssertionFails() {
        var root = new IllegalArgumentException("root msg");
        var top = new RuntimeException("top", new IOException("mid", root));
        assertThrows(AssertionError.class,
                () -> checkThatThrownBy(() -> { throw top; }).rootCause().hasMessage("wrong"));
    }
}
