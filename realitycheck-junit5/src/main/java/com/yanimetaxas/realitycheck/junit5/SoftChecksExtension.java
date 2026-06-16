package com.yanimetaxas.realitycheck.junit5;

import com.yanimetaxas.realitycheck.SoftChecks;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.opentest4j.MultipleFailuresError;

import java.util.Arrays;
import java.util.List;

/**
 * JUnit 5 extension that injects a {@link SoftChecks} parameter into test methods
 * and automatically calls {@code assertAll()} after each test.
 *
 * <p>Combined failures are reported as {@link MultipleFailuresError} so that IDEs
 * (IntelliJ, Eclipse) and JUnit's console renderer display each failure in its own
 * expected/actual diff pane, rather than as a single opaque {@code AssertionError}.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * @ExtendWith(SoftChecksExtension.class)
 * class MyTest {
 *     @Test
 *     void allFieldsValid(SoftChecks softly) {
 *         softly.checkThat("name").isNotEmpty();
 *         softly.checkThat(42).isPositive();
 *         softly.checkThat(List.of(1, 2)).hasSize(2);
 *         // assertAll() is called automatically after the test
 *     }
 * }
 * }</pre>
 *
 * Or use the {@link WithSoftChecks} annotation as a shorthand:
 * <pre>{@code
 * @WithSoftChecks
 * class MyTest { ... }
 * }</pre>
 */
public class SoftChecksExtension implements ParameterResolver, AfterEachCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(SoftChecksExtension.class);
    private static final String KEY = "softChecks";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == SoftChecks.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) {
        return extensionContext.getStore(NAMESPACE)
                .getOrComputeIfAbsent(KEY, k -> new SoftChecks(), SoftChecks.class);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        SoftChecks softChecks = context.getStore(NAMESPACE).remove(KEY, SoftChecks.class);
        if (softChecks == null) {
            return;
        }
        try {
            softChecks.assertAll();
        } catch (AssertionError e) {
            List<Throwable> failures = Arrays.asList(e.getSuppressed());
            throw new MultipleFailuresError(
                    "Multiple assertion failures (" + failures.size() + ")", failures);
        }
    }
}
