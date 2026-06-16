package com.yanimetaxas.realitycheck;

import java.math.BigInteger;

/**
 * Fluent assertions for {@link BigInteger} values.
 *
 * <pre>{@code
 * checkThat(factorial).isPositive().isGreaterThan(BigInteger.TEN);
 * checkThat(count).isEven().hasBitLength(32);
 * }</pre>
 */
public final class BigIntegerCheck extends OrderedCheck<BigIntegerCheck, BigInteger> {

    BigIntegerCheck(BigInteger actual, FailureHandler handler) {
        super(actual, handler);
    }

    public BigIntegerCheck isZero() {
        return failureHandler().check(self(), actual().signum() == 0,
                "expected zero but was: <%s>", actual());
    }

    public BigIntegerCheck isNotZero() {
        return failureHandler().check(self(), actual().signum() != 0,
                "expected a non-zero value");
    }

    public BigIntegerCheck isPositive() {
        return failureHandler().check(self(), actual().signum() > 0,
                "expected a positive value but was: <%s>", actual());
    }

    public BigIntegerCheck isNegative() {
        return failureHandler().check(self(), actual().signum() < 0,
                "expected a negative value but was: <%s>", actual());
    }

    public BigIntegerCheck isEven() {
        return failureHandler().check(self(), !actual().testBit(0),
                "expected an even value but was: <%s>", actual());
    }

    public BigIntegerCheck isOdd() {
        return failureHandler().check(self(), actual().testBit(0),
                "expected an odd value but was: <%s>", actual());
    }

    public BigIntegerCheck isProbablePrime() {
        return failureHandler().check(self(), actual().isProbablePrime(20),
                "expected a probable prime but was: <%s>", actual());
    }

    public BigIntegerCheck hasBitLength(int expected) {
        return failureHandler().check(self(), actual().bitLength() == expected,
                "expected bit length <%d> but was: <%d>", expected, actual().bitLength());
    }
}
