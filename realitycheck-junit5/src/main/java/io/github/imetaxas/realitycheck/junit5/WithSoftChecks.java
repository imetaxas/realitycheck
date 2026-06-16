package io.github.imetaxas.realitycheck.junit5;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Shorthand for {@code @ExtendWith(SoftChecksExtension.class)}.
 *
 * <pre>{@code
 * @WithSoftChecks
 * class MyTest {
 *     @Test
 *     void example(SoftChecks softly) {
 *         softly.checkThat("hello").isNotEmpty();
 *         softly.checkThat(42).isPositive();
 *     }
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SoftChecksExtension.class)
public @interface WithSoftChecks {
}
