package io.github.imetaxas.realitycheck;

import static io.github.imetaxas.realitycheck.Reality.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CheckInterfaceTest {

    @Test
    void isNull_passesAndFails() {
        assertDoesNotThrow(() -> checkThatObject(null).isNull());
        assertThrows(AssertionError.class, () -> checkThatObject("not null").isNull());
    }

    @Test
    void isSameAs_passesAndFails() {
        Object a = new Object();
        assertDoesNotThrow(() -> checkThatObject(a).isSameAs(a));
        assertThrows(AssertionError.class, () -> checkThatObject(a).isSameAs(new Object()));
    }

    @Test
    void isNotSameAs_passesAndFails() {
        Object a = new Object();
        Object b = new Object();
        assertDoesNotThrow(() -> checkThatObject(a).isNotSameAs(b));
        assertThrows(AssertionError.class, () -> checkThatObject(a).isNotSameAs(a));
    }

    @Test
    void matches_nullPredicate_throwsNPE() {
        java.util.function.Predicate<Integer> nullPred = null;
        assertThrows(NullPointerException.class,
                () -> checkThat(42).matches(nullPred));
    }

    @Test
    void satisfies_nullPredicate_throwsNPE() {
        java.util.function.Predicate<Integer> nullPred = null;
        assertThrows(NullPointerException.class,
                () -> checkThat(42).satisfies(nullPred, "desc"));
    }

    @Test
    void satisfies_nullConsumer_throwsNPE() {
        assertThrows(NullPointerException.class,
                () -> checkThat(42).satisfies((java.util.function.Consumer<Integer>) null));
    }

    @Test
    void isInstanceOf_nullType_throwsNPE() {
        assertThrows(NullPointerException.class,
                () -> checkThat(42).isInstanceOf(null));
    }

    @Test
    void isInstanceOf_failsOnWrongType() {
        assertThrows(AssertionError.class,
                () -> checkThat(42).isInstanceOf(String.class));
    }

    @Test
    void isNotNull_failsOnNull() {
        assertThrows(AssertionError.class,
                () -> checkThatObject(null).isNotNull());
    }

    @Test
    void isNull_failsOnNonNull() {
        assertThrows(AssertionError.class,
                () -> checkThatObject("hello").isNull());
    }

    @Test
    void isEqualTo_failsOnDifferent() {
        assertThrows(AssertionError.class,
                () -> checkThatObject("hello").isEqualTo("world"));
    }

    @Test
    void isNotEqualTo_failsOnSame() {
        assertThrows(AssertionError.class,
                () -> checkThatObject("hello").isNotEqualTo("hello"));
    }

    @Test
    void isSameAs_failsOnDifferentInstance() {
        assertThrows(AssertionError.class,
                () -> checkThatObject(new Object()).isSameAs(new Object()));
    }

    @Test
    void isNotSameAs_failsOnSameInstance() {
        Object obj = new Object();
        assertThrows(AssertionError.class,
                () -> checkThatObject(obj).isNotSameAs(obj));
    }

    @Test
    void matches_failsOnNonMatchingPredicate() {
        assertThrows(AssertionError.class,
                () -> checkThat(42).matches(n -> n > 100));
    }

    @Test
    void satisfies_failsOnNonMatchingPredicate() {
        assertThrows(AssertionError.class,
                () -> checkThat(42).satisfies(n -> n > 100, "gt 100"));
    }
}
