package io.github.imetaxas.realitycheck.junit5;

import io.github.imetaxas.realitycheck.SoftChecks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.opentest4j.MultipleFailuresError;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.junit.jupiter.api.extension.ParameterContext;

import static org.junit.jupiter.api.Assertions.*;

@WithSoftChecks
class SoftChecksExtensionTest {

    @Test
    void allPass_noFailure(SoftChecks softly) {
        softly.checkThat("hello").isNotEmpty();
        softly.checkThat(42).isPositive();
        softly.checkThat(List.of(1, 2, 3)).hasSize(3);
    }

    @Test
    void supportsParameter_returnsTrueForSoftChecks() throws Exception {
        var ext = new SoftChecksExtension();
        Method method = SoftChecksExtensionTest.class.getDeclaredMethod("allPass_noFailure", SoftChecks.class);
        java.lang.reflect.Parameter param = method.getParameters()[0];
        assertTrue(param.getType() == SoftChecks.class);
    }

    @Test
    void supportsParameter_returnsFalseForNonSoftChecksType() throws Exception {
        var ext = new SoftChecksExtension();
        Method method = SoftChecksExtensionTest.class.getDeclaredMethod("helperWithStringParam", String.class);
        Parameter param = method.getParameters()[0];
        ParameterContext paramCtx = new ParameterContext() {
            @Override public Parameter getParameter() { return param; }
            @Override public int getIndex() { return 0; }
            @Override public Optional<Object> getTarget() { return Optional.empty(); }
        };
        assertFalse(ext.supportsParameter(paramCtx, null));
    }

    @SuppressWarnings("unused")
    private void helperWithStringParam(String s) {}

    @Test
    void softCheckFailure_isReportedAfterTest(SoftChecks softly) {
        softly.checkThat("hello").isNotEmpty();
        softly.checkThat(42).isPositive();
    }

    @Test
    void afterEach_withFailures_throwsMultipleFailuresError() {
        var soft = new SoftChecks();
        soft.checkThat("").isNotEmpty();
        soft.checkThat(-1).isPositive();

        var ext = new SoftChecksExtension();
        var ctx = new FakeExtensionContext(soft);

        assertThrows(MultipleFailuresError.class, () -> ext.afterEach(ctx));
    }

    @Test
    void afterEach_withNoFailures_doesNotThrow() {
        var soft = new SoftChecks();
        soft.checkThat("ok").isNotEmpty();

        var ext = new SoftChecksExtension();
        var ctx = new FakeExtensionContext(soft);

        assertDoesNotThrow(() -> ext.afterEach(ctx));
    }

    @Test
    void afterEach_withNullSoftChecks_doesNotThrow() {
        var ext = new SoftChecksExtension();
        var ctx = new FakeExtensionContext(null);
        assertDoesNotThrow(() -> ext.afterEach(ctx));
    }

    /**
     * Minimal fake ExtensionContext that serves a fixed SoftChecks from an in-memory store.
     */
    private static final class FakeExtensionContext implements ExtensionContext {

        private final Map<Object, Object> data = new HashMap<>();

        FakeExtensionContext(SoftChecks soft) {
            data.put("softChecks", soft);
        }

        @Override
        public Store getStore(Namespace namespace) {
            return new Store() {
                @Override
                public Object get(Object key) { return data.get(key); }

                @SuppressWarnings("unchecked")
                @Override
                public <V> V get(Object key, Class<V> requiredType) {
                    return (V) data.get(key);
                }

                @SuppressWarnings("unchecked")
                @Override
                public <V> V remove(Object key, Class<V> requiredType) {
                    return (V) data.remove(key);
                }

                @Override
                public Object remove(Object key) { return data.remove(key); }

                @Override
                public void put(Object key, Object value) { data.put(key, value); }

                @SuppressWarnings("unchecked")
                @Override
                public <K, V> V getOrComputeIfAbsent(K key, Function<K, V> creator) {
                    return (V) data.computeIfAbsent(key, k -> creator.apply((K) k));
                }

                @SuppressWarnings("unchecked")
                @Override
                public <K, V> V getOrComputeIfAbsent(K key, Function<K, V> creator, Class<V> type) {
                    return (V) data.computeIfAbsent(key, k -> creator.apply((K) k));
                }
            };
        }

        // ── Unused ExtensionContext methods — all return empty/no-op ──

        @Override public Optional<java.lang.reflect.AnnotatedElement> getElement() { return Optional.empty(); }
        @Override public Optional<Class<?>> getTestClass() { return Optional.empty(); }
        @Override public Optional<java.lang.reflect.Method> getTestMethod() { return Optional.empty(); }
        @Override public Optional<Object> getTestInstance() { return Optional.empty(); }
        @Override public Optional<org.junit.jupiter.api.extension.TestInstances> getTestInstances() { return Optional.empty(); }
        @Override public Optional<Throwable> getExecutionException() { return Optional.empty(); }
        @Override public Optional<String> getConfigurationParameter(String key) { return Optional.empty(); }
        @Override public <T> Optional<T> getConfigurationParameter(String key, Function<String, T> transformer) { return Optional.empty(); }
        @Override public void publishReportEntry(Map<String, String> map) {}
        @Override public String getUniqueId() { return "fake"; }
        @Override public String getDisplayName() { return "fake"; }
        @Override public Set<String> getTags() { return Set.of(); }
        @Override public Optional<ExtensionContext> getParent() { return Optional.empty(); }
        @Override public ExtensionContext getRoot() { return this; }
        @Override public Optional<org.junit.jupiter.api.TestInstance.Lifecycle> getTestInstanceLifecycle() { return Optional.empty(); }
        @Override public org.junit.jupiter.api.parallel.ExecutionMode getExecutionMode() { return org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD; }
        @Override public org.junit.jupiter.api.extension.ExecutableInvoker getExecutableInvoker() { throw new UnsupportedOperationException(); }
    }
}
