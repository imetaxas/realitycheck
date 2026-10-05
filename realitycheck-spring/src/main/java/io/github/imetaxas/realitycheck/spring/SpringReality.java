package io.github.imetaxas.realitycheck.spring;

import io.github.imetaxas.realitycheck.FailureHandler;
import io.github.imetaxas.realitycheck.SoftChecks;
import org.springframework.context.ApplicationContext;

/**
 * Entry point for Spring {@link ApplicationContext} assertions.
 *
 * <p>Named {@code assertThatContext} (not {@code assertThat}) so it can be statically imported
 * alongside {@code RealityAssertions.assertThat} without overload clashes.
 *
 * <pre>{@code
 * import static io.github.imetaxas.realitycheck.RealityAssertions.assertThat;
 * import static io.github.imetaxas.realitycheck.spring.SpringReality.assertThatContext;
 *
 * assertThatContext(context).hasSingleBean(MyService.class);
 * assertThatContext(context).bean(MyService.class).isInstanceOf(MyServiceImpl.class);
 * }</pre>
 */
public final class SpringReality {

    private SpringReality() {}

    public static SpringContextCheck checkThatContext(ApplicationContext context) {
        return new SpringContextCheck(context, new FailureHandler());
    }

    /**
     * Same as {@link #checkThatContext(ApplicationContext)} but records failures on {@code
     * softly} so they participate in {@link SoftChecks#assertAll()}.
     */
    public static SpringContextCheck checkThatContext(
            ApplicationContext context, SoftChecks softly) {
        return new SpringContextCheck(context, softly.failureHandler());
    }

    /**
     * Same as {@link #checkThatContext(ApplicationContext)} with an explicit {@link
     * FailureHandler}.
     */
    public static SpringContextCheck checkThatContext(
            ApplicationContext context, FailureHandler handler) {
        return new SpringContextCheck(context, handler);
    }

    /** Alias for {@link #checkThatContext(ApplicationContext)} for AssertJ migration. */
    public static SpringContextCheck assertThatContext(ApplicationContext context) {
        return checkThatContext(context);
    }

    /** Alias for {@link #checkThatContext(ApplicationContext, SoftChecks)}. */
    public static SpringContextCheck assertThatContext(
            ApplicationContext context, SoftChecks softly) {
        return checkThatContext(context, softly);
    }
}
