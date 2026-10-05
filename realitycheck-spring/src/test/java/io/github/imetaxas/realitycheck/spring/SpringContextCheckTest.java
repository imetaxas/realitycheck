package io.github.imetaxas.realitycheck.spring;

import static io.github.imetaxas.realitycheck.spring.SpringReality.assertThatContext;
import static io.github.imetaxas.realitycheck.spring.SpringReality.checkThatContext;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.imetaxas.realitycheck.FailureHandler;
import io.github.imetaxas.realitycheck.ObjectCheck;
import io.github.imetaxas.realitycheck.ThrowableCheck;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class SpringContextCheckTest {

    private interface Service {}

    private static final class ServiceImpl implements Service {}

    /** Mirrors Spring Boot {@code AssertableApplicationContext#getStartupFailure()}. */
    private interface ContextWithStartupFailure extends ApplicationContext {
        Throwable getStartupFailure();
    }

    private interface ContextWithCheckedStartupFailure extends ApplicationContext {
        Throwable getStartupFailure() throws Exception;
    }

    @Test
    void hasSingleBean_passes() {
        ApplicationContext ctx = contextWith(Service.class, Map.of("svc", new ServiceImpl()));
        assertDoesNotThrow(() -> assertThatContext(ctx).hasSingleBean(Service.class));
    }

    @Test
    void hasSingleBean_failsWhenMissing() {
        ApplicationContext ctx = contextWith(Service.class, Map.of());
        AssertionError error =
                assertThrows(
                        AssertionError.class,
                        () -> assertThatContext(ctx).hasSingleBean(Service.class));
        assertTrue(error.getMessage().contains("expected a single bean of type"));
        assertTrue(error.getMessage().contains("found 0"));
    }

    @Test
    void hasSingleBean_failsWhenMultiple() {
        ApplicationContext ctx =
                contextWith(
                        Service.class,
                        Map.of("a", new ServiceImpl(), "b", new ServiceImpl()));
        AssertionError error =
                assertThrows(
                        AssertionError.class,
                        () -> assertThatContext(ctx).hasSingleBean(Service.class));
        assertTrue(error.getMessage().contains("found 2"));
        assertTrue(error.getMessage().contains("a"));
        assertTrue(error.getMessage().contains("b"));
    }

    @Test
    void hasSingleBean_failsForNullContext() {
        assertThrows(AssertionError.class, () -> assertThatContext(null).hasSingleBean(Service.class));
    }

    @Test
    void hasSingleBean_failsForNullType() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).hasSingleBean(null));
        assertTrue(error.getMessage().contains("type must not be null"));
    }

    @Test
    void hasBean_passesWhenContainsBean() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        when(ctx.containsBean("svc")).thenReturn(true);
        assertDoesNotThrow(() -> assertThatContext(ctx).hasBean("svc"));
    }

    @Test
    void hasBean_failsWhenMissing() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        when(ctx.containsBean("svc")).thenReturn(false);
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).hasBean("svc"));
        assertTrue(error.getMessage().contains("expected a bean named <svc>"));
    }

    @Test
    void hasBean_failsForNullContext() {
        assertThrows(AssertionError.class, () -> assertThatContext(null).hasBean("svc"));
    }

    @Test
    void hasBean_failsForNullName() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).hasBean(null));
        assertTrue(error.getMessage().contains("name must not be null"));
    }

    @Test
    void doesNotHaveBean_passes() {
        ApplicationContext ctx = contextWith(Service.class, Map.of());
        assertDoesNotThrow(() -> assertThatContext(ctx).doesNotHaveBean(Service.class));
    }

    @Test
    void doesNotHaveBean_failsWhenPresent() {
        ApplicationContext ctx = contextWith(Service.class, Map.of("svc", new ServiceImpl()));
        AssertionError error =
                assertThrows(
                        AssertionError.class,
                        () -> assertThatContext(ctx).doesNotHaveBean(Service.class));
        assertTrue(error.getMessage().contains("expected no bean of type"));
        assertTrue(error.getMessage().contains("found 1"));
        assertTrue(error.getMessage().contains("svc"));
    }

    @Test
    void doesNotHaveBean_failsForNullContext() {
        assertThrows(
                AssertionError.class, () -> assertThatContext(null).doesNotHaveBean(Service.class));
    }

    @Test
    void doesNotHaveBean_failsForNullType() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        AssertionError error =
                assertThrows(
                        AssertionError.class, () -> assertThatContext(ctx).doesNotHaveBean((Class<?>) null));
        assertTrue(error.getMessage().contains("type must not be null"));
    }

    @Test
    void doesNotHaveBeanByName_passesWhenMissing() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        when(ctx.containsBean("legacy")).thenReturn(false);
        assertDoesNotThrow(() -> assertThatContext(ctx).doesNotHaveBean("legacy"));
    }

    @Test
    void doesNotHaveBeanByName_failsWhenPresent() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        when(ctx.containsBean("legacy")).thenReturn(true);
        AssertionError error =
                assertThrows(
                        AssertionError.class, () -> assertThatContext(ctx).doesNotHaveBean("legacy"));
        assertTrue(error.getMessage().contains("expected no bean named <legacy>"));
    }

    @Test
    void doesNotHaveBeanByName_failsForNullContext() {
        assertThrows(AssertionError.class, () -> assertThatContext(null).doesNotHaveBean("legacy"));
    }

    @Test
    void doesNotHaveBeanByName_failsForNullName() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        AssertionError error =
                assertThrows(
                        AssertionError.class, () -> assertThatContext(ctx).doesNotHaveBean((String) null));
        assertTrue(error.getMessage().contains("name must not be null"));
    }

    @Test
    void bean_returnsUniqueInstance() {
        ServiceImpl impl = new ServiceImpl();
        ApplicationContext ctx = contextWith(Service.class, Map.of("svc", impl));
        assertDoesNotThrow(
                () -> assertThatContext(ctx).bean(Service.class).isSameAs(impl).isInstanceOf(ServiceImpl.class));
    }

    @Test
    void bean_failsWhenMissing() {
        ApplicationContext ctx = contextWith(Service.class, Map.of());
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).bean(Service.class));
        assertTrue(error.getMessage().contains("found 0"));
    }

    @Test
    void bean_failsWhenMultiple() {
        ApplicationContext ctx =
                contextWith(
                        Service.class,
                        Map.of("a", new ServiceImpl(), "b", new ServiceImpl()));
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).bean(Service.class));
        assertTrue(error.getMessage().contains("found 2"));
    }

    @Test
    void bean_failsWhenGetBeanCannotPickPrimary() {
        ApplicationContext ctx =
                contextWith(
                        Service.class,
                        Map.of("a", new ServiceImpl(), "b", new ServiceImpl()));
        when(ctx.getBean(Service.class))
                .thenThrow(
                        new org.springframework.beans.factory.NoUniqueBeanDefinitionException(
                                Service.class, "a", "b"));
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).bean(Service.class));
        assertTrue(error.getMessage().contains("found 2"));
    }

    @Test
    void bean_failsForNullContext() {
        assertThrows(AssertionError.class, () -> assertThatContext(null).bean(Service.class));
    }

    @Test
    void bean_failsForNullType() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).bean(null));
        assertTrue(error.getMessage().contains("type must not be null"));
    }

    @Test
    void hasFailed_passesWhenStartupFailurePresent() {
        IllegalStateException boom = new IllegalStateException("refresh failed");
        ContextWithStartupFailure ctx = mock(ContextWithStartupFailure.class);
        when(ctx.getStartupFailure()).thenReturn(boom);
        assertDoesNotThrow(() -> assertThatContext(ctx).hasFailed());
    }

    @Test
    void hasFailed_failsWhenNoStartupFailure() {
        ContextWithStartupFailure ctx = mock(ContextWithStartupFailure.class);
        when(ctx.getStartupFailure()).thenReturn(null);
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).hasFailed());
        assertTrue(error.getMessage().contains("expected the ApplicationContext to have failed to start"));
    }

    @Test
    void hasFailed_failsForPlainContextWithoutStartupFailureMethod() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).hasFailed());
        assertTrue(error.getMessage().contains("expected the ApplicationContext to have failed to start"));
    }

    @Test
    void hasFailed_failsForNullContext() {
        assertThrows(AssertionError.class, () -> assertThatContext(null).hasFailed());
    }

    @Test
    void hasNotFailed_passesWhenNoStartupFailure() {
        ContextWithStartupFailure ctx = mock(ContextWithStartupFailure.class);
        when(ctx.getStartupFailure()).thenReturn(null);
        assertDoesNotThrow(() -> assertThatContext(ctx).hasNotFailed());
    }

    @Test
    void hasNotFailed_passesForPlainContext() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        assertDoesNotThrow(() -> assertThatContext(ctx).hasNotFailed());
    }

    @Test
    void hasNotFailed_failsWhenStartupFailurePresent() {
        IllegalStateException boom = new IllegalStateException("refresh failed");
        ContextWithStartupFailure ctx = mock(ContextWithStartupFailure.class);
        when(ctx.getStartupFailure()).thenReturn(boom);
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).hasNotFailed());
        assertTrue(error.getMessage().contains("started successfully but it failed"));
        assertTrue(error.getMessage().contains("refresh failed"));
    }

    @Test
    void hasNotFailed_failsForNullContext() {
        assertThrows(AssertionError.class, () -> assertThatContext(null).hasNotFailed());
    }

    @Test
    void failure_continuesOnStartupThrowable() {
        IllegalStateException boom = new IllegalStateException("refresh failed");
        ContextWithStartupFailure ctx = mock(ContextWithStartupFailure.class);
        when(ctx.getStartupFailure()).thenReturn(boom);
        assertDoesNotThrow(
                () ->
                        assertThatContext(ctx)
                                .failure()
                                .isSameAs(boom)
                                .hasMessageContaining("refresh failed"));
    }

    @Test
    void failure_failsWhenContextStarted() {
        ApplicationContext ctx = mock(ApplicationContext.class);
        AssertionError error =
                assertThrows(AssertionError.class, () -> assertThatContext(ctx).failure());
        assertTrue(error.getMessage().contains("expected a startup failure"));
    }

    @Test
    void failure_failsForNullContext() {
        assertThrows(AssertionError.class, () -> assertThatContext(null).failure());
    }

    @Test
    void hasFailed_rethrowsRuntimeExceptionFromStartupFailureLookup() {
        ContextWithStartupFailure ctx = mock(ContextWithStartupFailure.class);
        when(ctx.getStartupFailure()).thenThrow(new IllegalStateException("lookup failed"));
        IllegalStateException error =
                assertThrows(
                        IllegalStateException.class, () -> assertThatContext(ctx).hasFailed());
        assertTrue(error.getMessage().contains("lookup failed"));
    }

    @Test
    void hasFailed_wrapsCheckedExceptionFromStartupFailureLookup() throws Exception {
        ContextWithCheckedStartupFailure ctx = mock(ContextWithCheckedStartupFailure.class);
        doThrow(new Exception("checked lookup")).when(ctx).getStartupFailure();
        IllegalStateException error =
                assertThrows(
                        IllegalStateException.class, () -> assertThatContext(ctx).hasFailed());
        assertTrue(error.getCause().getMessage().contains("checked lookup"));
    }

    @Test
    void checkThatContext_isEquivalentAlias() {
        ApplicationContext ctx = contextWith(Service.class, Map.of("svc", new ServiceImpl()));
        assertDoesNotThrow(() -> checkThatContext(ctx).hasSingleBean(Service.class));
    }

    @Test
    void as_prefixesFailureMessage() {
        ApplicationContext ctx = contextWith(Service.class, Map.of());
        AssertionError error =
                assertThrows(
                        AssertionError.class,
                        () ->
                                assertThatContext(ctx)
                                        .as("sqlsage4j bean")
                                        .hasSingleBean(Service.class));
        assertTrue(error.getMessage().startsWith("[sqlsage4j bean]"));
    }

    @Test
    void hasSingleBean_returnsSelfWhenHandlerDoesNotThrow() {
        CollectingHandler handler = new CollectingHandler();
        ApplicationContext ctx = contextWith(Service.class, Map.of());
        SpringContextCheck check = new SpringContextCheck(ctx, handler);
        assertThatSameInstance(check.hasSingleBean(Service.class), check);
        assertThatSameInstance(check.hasSingleBean(null), check);
        SpringContextCheck nullContext = new SpringContextCheck(null, handler);
        assertThatSameInstance(nullContext.hasSingleBean(Service.class), nullContext);
    }

    @Test
    void hasBean_returnsSelfWhenHandlerDoesNotThrow() {
        CollectingHandler handler = new CollectingHandler();
        ApplicationContext ctx = mock(ApplicationContext.class);
        when(ctx.containsBean("svc")).thenReturn(false);
        SpringContextCheck check = new SpringContextCheck(ctx, handler);
        assertThatSameInstance(check.hasBean("svc"), check);
        assertThatSameInstance(check.hasBean(null), check);
        SpringContextCheck nullContext = new SpringContextCheck(null, handler);
        assertThatSameInstance(nullContext.hasBean("svc"), nullContext);
    }

    @Test
    void doesNotHaveBean_returnsSelfWhenHandlerDoesNotThrow() {
        CollectingHandler handler = new CollectingHandler();
        ApplicationContext ctx = contextWith(Service.class, Map.of("svc", new ServiceImpl()));
        SpringContextCheck check = new SpringContextCheck(ctx, handler);
        assertThatSameInstance(check.doesNotHaveBean(Service.class), check);
        assertThatSameInstance(check.doesNotHaveBean((Class<?>) null), check);
        SpringContextCheck nullContext = new SpringContextCheck(null, handler);
        assertThatSameInstance(nullContext.doesNotHaveBean(Service.class), nullContext);
    }

    @Test
    void doesNotHaveBeanByName_returnsSelfWhenHandlerDoesNotThrow() {
        CollectingHandler handler = new CollectingHandler();
        ApplicationContext ctx = mock(ApplicationContext.class);
        when(ctx.containsBean("legacy")).thenReturn(true);
        SpringContextCheck check = new SpringContextCheck(ctx, handler);
        assertThatSameInstance(check.doesNotHaveBean("legacy"), check);
        assertThatSameInstance(check.doesNotHaveBean((String) null), check);
        SpringContextCheck nullContext = new SpringContextCheck(null, handler);
        assertThatSameInstance(nullContext.doesNotHaveBean("legacy"), nullContext);
    }

    @Test
    void bean_returnsObjectCheckWhenHandlerDoesNotThrow() {
        CollectingHandler handler = new CollectingHandler();
        ApplicationContext missing = contextWith(Service.class, Map.of());
        SpringContextCheck check = new SpringContextCheck(missing, handler);
        ObjectCheck<Service> fromMissing = check.bean(Service.class);
        ObjectCheck<Service> fromNullType = check.bean(null);
        SpringContextCheck nullContext = new SpringContextCheck(null, handler);
        ObjectCheck<Service> fromNullContext = nullContext.bean(Service.class);
        assertSame(handler, fromMissing.failureHandler());
        assertSame(handler, fromNullType.failureHandler());
        assertSame(handler, fromNullContext.failureHandler());
    }

    @Test
    void hasFailed_returnsSelfWhenHandlerDoesNotThrow() {
        CollectingHandler handler = new CollectingHandler();
        ApplicationContext ctx = mock(ApplicationContext.class);
        SpringContextCheck check = new SpringContextCheck(ctx, handler);
        assertThatSameInstance(check.hasFailed(), check);
        SpringContextCheck nullContext = new SpringContextCheck(null, handler);
        assertThatSameInstance(nullContext.hasFailed(), nullContext);
    }

    @Test
    void hasNotFailed_returnsSelfWhenHandlerDoesNotThrow() {
        CollectingHandler handler = new CollectingHandler();
        ContextWithStartupFailure ctx = mock(ContextWithStartupFailure.class);
        when(ctx.getStartupFailure()).thenReturn(new IllegalStateException("boom"));
        SpringContextCheck check = new SpringContextCheck(ctx, handler);
        assertThatSameInstance(check.hasNotFailed(), check);
        SpringContextCheck nullContext = new SpringContextCheck(null, handler);
        assertThatSameInstance(nullContext.hasNotFailed(), nullContext);
    }

    @Test
    void failure_returnsThrowableCheckWhenHandlerDoesNotThrow() {
        CollectingHandler handler = new CollectingHandler();
        ApplicationContext ctx = mock(ApplicationContext.class);
        SpringContextCheck check = new SpringContextCheck(ctx, handler);
        ThrowableCheck fromStarted = check.failure();
        SpringContextCheck nullContext = new SpringContextCheck(null, handler);
        ThrowableCheck fromNull = nullContext.failure();
        assertSame(handler, fromStarted.failureHandler());
        assertSame(handler, fromNull.failureHandler());
    }

    private static void assertThatSameInstance(SpringContextCheck actual, SpringContextCheck expected) {
        if (actual != expected) {
            throw new AssertionError("expected the same check instance for fluent chaining");
        }
    }

    private static final class CollectingHandler extends FailureHandler {
        @Override
        public void fail(String format, Object... args) {
            // Soft-style: record only, so return-self paths after fail() are exercised.
        }
    }

    @SuppressWarnings("unchecked")
    private static ApplicationContext contextWith(Class<?> type, Map<String, ?> beans) {
        ApplicationContext ctx = mock(ApplicationContext.class);
        when(ctx.getBeansOfType((Class<Object>) type))
                .thenReturn(new LinkedHashMap<>((Map<String, Object>) beans));
        return ctx;
    }
}
