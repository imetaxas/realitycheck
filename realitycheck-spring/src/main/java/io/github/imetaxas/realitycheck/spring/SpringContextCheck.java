package io.github.imetaxas.realitycheck.spring;

import io.github.imetaxas.realitycheck.AbstractCheck;
import io.github.imetaxas.realitycheck.FailureHandler;
import io.github.imetaxas.realitycheck.ObjectCheck;
import io.github.imetaxas.realitycheck.ThrowableCheck;
import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Fluent assertions for a Spring {@link ApplicationContext}, covering the AssertJ {@code
 * ApplicationContextAssert} methods used in typical auto-configuration tests.
 *
 * <pre>{@code
 * import static io.github.imetaxas.realitycheck.spring.SpringReality.assertThatContext;
 *
 * assertThatContext(context).hasSingleBean(MyService.class);
 * assertThatContext(context).doesNotHaveBean(LegacyService.class);
 * assertThatContext(context).bean(MyService.class).isInstanceOf(MyServiceImpl.class);
 * assertThatContext(context).hasBean("myService").doesNotHaveBean("legacyService");
 * assertThatContext(context).hasNotFailed();
 * }</pre>
 *
 * <p>{@link #hasFailed()}, {@link #hasNotFailed()}, and {@link #failure()} prefer {@code
 * getStartupFailure()} when the context exposes that method (Spring Boot {@code
 * ApplicationContextRunner}). Otherwise a {@link ConfigurableApplicationContext} is treated as
 * failed when {@link ConfigurableApplicationContext#isActive()} is {@code false}.
 */
public final class SpringContextCheck extends AbstractCheck<SpringContextCheck, ApplicationContext> {

    SpringContextCheck(ApplicationContext actual, FailureHandler handler) {
        super(actual, handler);
    }

    /**
     * Asserts that the context contains exactly one bean of {@code type} (including subclasses),
     * using {@link ApplicationContext#getBeansOfType(Class)}.
     *
     * @param type the bean type to look up; must not be {@code null}
     * @return {@code this}
     */
    public SpringContextCheck hasSingleBean(Class<?> type) {
        if (!isActualPresent()) {
            return self();
        }
        if (type == null) {
            failureHandler().fail("hasSingleBean: type must not be null");
            return self();
        }
        Map<String, ?> beans = actual().getBeansOfType(type);
        return failureHandler()
                .check(
                        self(),
                        beans.size() == 1,
                        "expected a single bean of type <%s> but found %d: %s",
                        type.getName(),
                        beans.size(),
                        beans.keySet());
    }

    /**
     * Asserts that a bean named {@code name} exists, using {@link ApplicationContext#containsBean}.
     *
     * @param name the bean name (or alias); must not be {@code null}
     * @return {@code this}
     */
    public SpringContextCheck hasBean(String name) {
        if (!isActualPresent()) {
            return self();
        }
        if (name == null) {
            failureHandler().fail("hasBean: name must not be null");
            return self();
        }
        return failureHandler()
                .check(self(), actual().containsBean(name), "expected a bean named <%s>", name);
    }

    /**
     * Asserts that the context contains no beans of {@code type} (including subclasses), using
     * {@link ApplicationContext#getBeansOfType(Class)}.
     *
     * @param type the bean type to look up; must not be {@code null}
     * @return {@code this}
     */
    public SpringContextCheck doesNotHaveBean(Class<?> type) {
        if (!isActualPresent()) {
            return self();
        }
        if (type == null) {
            failureHandler().fail("doesNotHaveBean: type must not be null");
            return self();
        }
        Map<String, ?> beans = actual().getBeansOfType(type);
        return failureHandler()
                .check(
                        self(),
                        beans.isEmpty(),
                        "expected no bean of type <%s> but found %d: %s",
                        type.getName(),
                        beans.size(),
                        beans.keySet());
    }

    /**
     * Asserts that no bean named {@code name} exists, using {@link ApplicationContext#containsBean}.
     *
     * @param name the bean name (or alias); must not be {@code null}
     * @return {@code this}
     */
    public SpringContextCheck doesNotHaveBean(String name) {
        if (!isActualPresent()) {
            return self();
        }
        if (name == null) {
            failureHandler().fail("doesNotHaveBean: name must not be null");
            return self();
        }
        return failureHandler()
                .check(
                        self(),
                        !actual().containsBean(name),
                        "expected no bean named <%s>",
                        name);
    }

    /**
     * Continues assertions on a bean of {@code type}. If several beans exist, {@link
     * ApplicationContext#getBean(Class)} is used so a unique {@code @Primary} bean is accepted.
     *
     * @param type the bean type to look up; must not be {@code null}
     * @return an {@link ObjectCheck} over the bean, or over {@code null} after a failure
     */
    public <T> ObjectCheck<T> bean(Class<T> type) {
        if (!isActualPresent()) {
            return ObjectCheck.of(null, failureHandler());
        }
        if (type == null) {
            failureHandler().fail("bean: type must not be null");
            return ObjectCheck.of(null, failureHandler());
        }
        Map<String, T> beans = actual().getBeansOfType(type);
        if (beans.size() == 1) {
            return ObjectCheck.of(beans.values().iterator().next(), failureHandler());
        }
        if (!beans.isEmpty()) {
            try {
                T candidate = actual().getBean(type);
                if (candidate != null) {
                    return ObjectCheck.of(candidate, failureHandler());
                }
            } catch (NoSuchBeanDefinitionException ignored) {
                // Includes NoUniqueBeanDefinitionException. Fall through to uniqueness failure.
            }
        }
        failureHandler()
                .fail(
                        "expected a single bean of type <%s> but found %d: %s",
                        type.getName(), beans.size(), beans.keySet());
        return ObjectCheck.of(null, failureHandler());
    }

    /**
     * Asserts that context refresh failed.
     *
     * @return {@code this}
     */
    public SpringContextCheck hasFailed() {
        if (!isActualPresent()) {
            return self();
        }
        return failureHandler()
                .check(
                        self(),
                        contextFailed(),
                        "expected the ApplicationContext to have failed to start");
    }

    /**
     * Asserts that context refresh succeeded.
     *
     * @return {@code this}
     */
    public SpringContextCheck hasNotFailed() {
        if (!isActualPresent()) {
            return self();
        }
        Throwable failure = startupFailure();
        return failureHandler()
                .check(
                        self(),
                        !contextFailed(),
                        "expected the ApplicationContext to have started successfully but it failed: %s",
                        failure != null ? failure : "context is not active");
    }

    /**
     * Asserts that context refresh failed and continues assertions on that throwable. Requires
     * {@code getStartupFailure()} (Spring Boot {@code ApplicationContextRunner}).
     *
     * @return a {@link ThrowableCheck} over the startup failure, or over {@code null} after a
     *     failure
     */
    public ThrowableCheck failure() {
        if (!isActualPresent()) {
            return ThrowableCheck.of(null, failureHandler());
        }
        Throwable failure = startupFailure();
        if (failure == null) {
            failureHandler()
                    .fail(
                            "expected a startup failure but the ApplicationContext started successfully"
                                    + (contextFailed()
                                            ? " (context is inactive but has no getStartupFailure())"
                                            : ""));
            return ThrowableCheck.of(null, failureHandler());
        }
        return ThrowableCheck.of(failure, failureHandler());
    }

    private boolean contextFailed() {
        if (hasStartupFailureMethod()) {
            return startupFailure() != null;
        }
        if (actual() instanceof ConfigurableApplicationContext configurable) {
            return !configurable.isActive();
        }
        return false;
    }

    private boolean hasStartupFailureMethod() {
        try {
            actual().getClass().getMethod("getStartupFailure");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Reads {@code getStartupFailure()} when present. Missing method means no captured throwable.
     */
    private Throwable startupFailure() {
        try {
            Method method = actual().getClass().getMethod("getStartupFailure");
            return (Throwable) method.invoke(actual());
        } catch (NoSuchMethodException e) {
            return null;
        } catch (ReflectiveOperationException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            if (cause instanceof RuntimeException runtime) {
                throw runtime;
            }
            throw new IllegalStateException(cause);
        }
    }
}
