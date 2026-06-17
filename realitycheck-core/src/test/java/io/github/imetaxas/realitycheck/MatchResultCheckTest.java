package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MatchResultCheckTest {

    @Test
    void hasGroupCount_passes() {
        assertDoesNotThrow(() ->
                checkThat("ERROR 42 something happened")
                        .matchesAndCaptures("(\\w+) (\\d+) (.+)")
                        .hasGroupCount(3));
    }

    @Test
    void group_byIndex_passes() {
        assertDoesNotThrow(() ->
                checkThat("ERROR 42 something")
                        .matchesAndCaptures("(\\w+) (\\d+) (.+)")
                        .group(1).isEqualTo("ERROR"));
    }

    @Test
    void group_byIndex_secondGroup() {
        assertDoesNotThrow(() ->
                checkThat("ERROR 42 something")
                        .matchesAndCaptures("(\\w+) (\\d+) (.+)")
                        .group(2).isEqualTo("42"));
    }

    @Test
    void group_byName_passes() {
        assertDoesNotThrow(() ->
                checkThat("2026-03-31")
                        .matchesAndCaptures("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})")
                        .group("year").isEqualTo("2026"));
    }

    @Test
    void group_byName_month() {
        assertDoesNotThrow(() ->
                checkThat("2026-03-31")
                        .matchesAndCaptures("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})")
                        .group("month").isEqualTo("03"));
    }

    @Test
    void hasGroup_byIndex_passes() {
        assertDoesNotThrow(() ->
                checkThat("hello world")
                        .matchesAndCaptures("(\\w+) (\\w+)")
                        .hasGroup(1, "hello")
                        .hasGroup(2, "world"));
    }

    @Test
    void hasGroup_byName_passes() {
        assertDoesNotThrow(() ->
                checkThat("John:30")
                        .matchesAndCaptures("(?<name>\\w+):(?<age>\\d+)")
                        .hasGroup("name", "John")
                        .hasGroup("age", "30"));
    }

    @Test
    void matchesAndCaptures_failsWhenNoMatch() {
        assertThrows(AssertionError.class, () ->
                checkThat("no match here")
                        .matchesAndCaptures("^\\d+$"));
    }

    @Test
    void group_byName_failsOnBadName() {
        assertThrows(AssertionError.class, () ->
                checkThat("hello")
                        .matchesAndCaptures("(\\w+)")
                        .group("nonexistent").isEqualTo("hello"));
    }

    @Test
    void group_outOfBounds() {
        assertThrows(AssertionError.class, () ->
                checkThat("hello")
                        .matchesAndCaptures("(\\w+)")
                        .group(99).isEqualTo("x"));
    }

    @Test
    void group_byName_pass() {
        assertDoesNotThrow(() ->
                checkThat("2026-03").matchesAndCaptures("(?<year>\\d{4})-(?<month>\\d{2})")
                        .group("year").isEqualTo("2026"));
    }

    @Test
    void group_byName_invalidName() {
        assertThrows(AssertionError.class, () ->
                checkThat("2026-03").matchesAndCaptures("(?<year>\\d{4})-(?<month>\\d{2})")
                        .group("nonexistent"));
    }

    @Test
    void hasGroup_int_pass() {
        assertDoesNotThrow(() ->
                checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                        .hasGroup(1, "abc"));
    }

    @Test
    void hasGroup_int_wrongValue() {
        assertThrows(AssertionError.class, () ->
                checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                        .hasGroup(1, "WRONG"));
    }

    @Test
    void hasGroup_int_outOfRange() {
        assertThrows(AssertionError.class, () ->
                checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                        .hasGroup(99, "whatever"));
    }

    @Test
    void hasGroup_string_pass() {
        assertDoesNotThrow(() ->
                checkThat("2026-03").matchesAndCaptures("(?<year>\\d{4})-(?<month>\\d{2})")
                        .hasGroup("year", "2026"));
    }

    @Test
    void hasGroup_string_wrongValue() {
        assertThrows(AssertionError.class, () ->
                checkThat("2026-03").matchesAndCaptures("(?<year>\\d{4})-(?<month>\\d{2})")
                        .hasGroup("year", "9999"));
    }

    @Test
    void hasGroup_string_invalidName() {
        assertThrows(AssertionError.class, () ->
                checkThat("2026-03").matchesAndCaptures("(?<year>\\d{4})-(?<month>\\d{2})")
                        .hasGroup("nope", "value"));
    }

    @Test
    void group_int_outOfRange() {
        assertThrows(AssertionError.class, () ->
                checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                        .group(99));
    }

    @Test
    void group_int_pass() {
        assertDoesNotThrow(() ->
                checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                        .group(2).isEqualTo("123"));
    }

    @Test
    void hasGroupCount_pass() {
        assertDoesNotThrow(() ->
                checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                        .hasGroupCount(2));
    }

    @Test
    void hasGroupCount_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                        .hasGroupCount(5));
    }

    @Test
    void noMatch_fails() {
        assertThrows(AssertionError.class, () ->
                checkThat("nothing").matchesAndCaptures("^\\d+$"));
    }

    @Test
    void from_nonMatching_fails() {
        assertThrows(AssertionError.class,
                () -> checkThat("hello").matchesAndCaptures("\\d+"));
    }

    @Nested
    class GroupByIndexEdgeCases {

        @Test
        void group_negativeIndex_fails() {
            assertThrows(AssertionError.class, () ->
                    checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                            .group(-1));
        }

        @Test
        void group_zeroIndex_returnsFullMatch() {
            assertDoesNotThrow(() ->
                    checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                            .group(0).isEqualTo("abc 123"));
        }

        @Test
        void group_exactBoundary_passes() {
            assertDoesNotThrow(() ->
                    checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                            .group(2).isEqualTo("123"));
        }

        @Test
        void group_onePastBoundary_fails() {
            assertThrows(AssertionError.class, () ->
                    checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                            .group(3));
        }

        @Test
        void group_optionalGroupNotParticipating_returnsNull() {
            assertDoesNotThrow(() -> {
                StringCheck result = checkThat("hello").matchesAndCaptures("(\\w+)(?:-(\\w+))?")
                        .group(2);
                assertNull(result.actual());
            });
        }
    }

    @Nested
    class GroupByNameEdgeCases {

        @Test
        void group_namedGroupWithEmptyValue() {
            assertDoesNotThrow(() ->
                    checkThat("key=").matchesAndCaptures("(?<key>\\w+)=(?<val>.*)")
                            .group("val").isEqualTo(""));
        }

        @Test
        void group_namedOptionalGroupNotParticipating() {
            assertDoesNotThrow(() -> {
                StringCheck result = checkThat("hello").matchesAndCaptures("(?<word>\\w+)(?:-(?<suffix>\\w+))?")
                        .group("suffix");
                assertNull(result.actual());
            });
        }
    }

    @Nested
    class HasGroupByIndexEdgeCases {

        @Test
        void hasGroup_negativeIndex_fails() {
            assertThrows(AssertionError.class, () ->
                    checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                            .hasGroup(-1, "anything"));
        }

        @Test
        void hasGroup_zeroIndex_fullMatch_passes() {
            assertDoesNotThrow(() ->
                    checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                            .hasGroup(0, "abc 123"));
        }

        @Test
        void hasGroup_zeroIndex_wrongValue_fails() {
            assertThrows(AssertionError.class, () ->
                    checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                            .hasGroup(0, "wrong"));
        }

        @Test
        void hasGroup_exactBoundary_passes() {
            assertDoesNotThrow(() ->
                    checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                            .hasGroup(2, "123"));
        }

        @Test
        void hasGroup_onePastBoundary_fails() {
            assertThrows(AssertionError.class, () ->
                    checkThat("abc 123").matchesAndCaptures("(\\w+) (\\d+)")
                            .hasGroup(3, "anything"));
        }

        @Test
        void hasGroup_optionalGroupNotParticipating_nullMismatch_fails() {
            assertThrows(AssertionError.class, () ->
                    checkThat("hello").matchesAndCaptures("(\\w+)(?:-(\\w+))?")
                            .hasGroup(2, "something"));
        }

        @Test
        void hasGroup_optionalGroupNotParticipating_expectNull_passes() {
            assertDoesNotThrow(() ->
                    checkThat("hello").matchesAndCaptures("(\\w+)(?:-(\\w+))?")
                            .hasGroup(2, null));
        }
    }

    @Nested
    class HasGroupByNameEdgeCases {

        @Test
        void hasGroup_namedGroupWithEmptyValue_passes() {
            assertDoesNotThrow(() ->
                    checkThat("key=").matchesAndCaptures("(?<key>\\w+)=(?<val>.*)")
                            .hasGroup("val", ""));
        }

        @Test
        void hasGroup_namedGroupWithEmptyValue_wrongExpected_fails() {
            assertThrows(AssertionError.class, () ->
                    checkThat("key=").matchesAndCaptures("(?<key>\\w+)=(?<val>.*)")
                            .hasGroup("val", "notempty"));
        }

        @Test
        void hasGroup_namedOptionalGroupNotParticipating_expectNull_passes() {
            assertDoesNotThrow(() ->
                    checkThat("hello").matchesAndCaptures("(?<word>\\w+)(?:-(?<suffix>\\w+))?")
                            .hasGroup("suffix", null));
        }

        @Test
        void hasGroup_namedOptionalGroupNotParticipating_expectValue_fails() {
            assertThrows(AssertionError.class, () ->
                    checkThat("hello").matchesAndCaptures("(?<word>\\w+)(?:-(?<suffix>\\w+))?")
                            .hasGroup("suffix", "x"));
        }
    }

    @Nested
    class GroupIndexSoftMode {
        // In strict mode fail() throws before the defensive ternary in group(int) runs.
        // Soft mode covers the `index >= 0 && index <= groupCount` false branches.

        @Test
        void group_negativeIndex_softMode_returnsNullStringCheck() {
            var handler = new SoftFailureHandler();
            MatchResultCheck check = MatchResultCheck.from("hello", "(\\w+)", handler);
            StringCheck result = check.group(-1);
            assertNull(result.actual());
            assertEquals(1, handler.failures().size());
        }

        @Test
        void group_outOfRangePositive_softMode_returnsNullStringCheck() {
            var handler = new SoftFailureHandler();
            MatchResultCheck check = MatchResultCheck.from("hello", "(\\w+)", handler);
            StringCheck result = check.group(99);
            assertNull(result.actual());
            assertEquals(1, handler.failures().size());
        }
    }
}
