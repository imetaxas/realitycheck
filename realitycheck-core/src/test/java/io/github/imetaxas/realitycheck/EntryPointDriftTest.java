package io.github.imetaxas.realitycheck;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards against entry-point drift. All four public entry-points must stay in sync
 * whenever a new type overload is added.
 *
 * <ul>
 *   <li>{@link Reality} and {@link SoftChecks} — {@code checkThat*} names must match.
 *   <li>{@link Reality} and {@link RealityAssertions} — {@code assertThat*} names must match.
 *   <li>{@link Reality} and {@link StatementBuilder} — every first-parameter type accepted by
 *       {@code Reality.checkThat*(T)} must also appear in some {@code StatementBuilder.that*(T)}
 *       overload (by exact parameter type), with the exception of {@link ThrowingCallable} which
 *       is intentionally absent from StatementBuilder.
 * </ul>
 */
class EntryPointDriftTest {

    @Test
    void softChecks_exposesAllCheckMethodsFromReality() {
        // checkAll/checkWithMessage/checkWithContext are static factory utilities on Reality,
        // intentionally absent from SoftChecks (SoftChecks itself IS the soft context).
        Set<String> realityCheckMethods = methodsStartingWith(Reality.class, "checkThat");
        Set<String> softCheckMethods = methodsStartingWith(SoftChecks.class, "checkThat");

        Set<String> missing = new HashSet<>(realityCheckMethods);
        missing.removeAll(softCheckMethods);

        assertTrue(missing.isEmpty(),
                "SoftChecks is missing check methods from Reality: " + missing);
    }

    @Test
    void realityAssertions_exposesAllAssertThatMethodsFromReality() {
        Set<String> realityAssertMethods = methodsStartingWith(Reality.class, "assertThat");
        Set<String> aliasAssertMethods = methodsStartingWith(RealityAssertions.class, "assertThat");

        Set<String> missing = new HashSet<>(realityAssertMethods);
        missing.removeAll(aliasAssertMethods);

        assertTrue(missing.isEmpty(),
                "RealityAssertions is missing assertThat methods from Reality: " + missing);
    }

    /**
     * For every first-parameter type accepted by {@code Reality.checkThat*(type)},
     * {@link StatementBuilder} must have a matching {@code that*(type)} overload.
     * This catches new primitive overloads (e.g. {@code float}, {@code float[]}) that
     * were added to Reality but forgotten in StatementBuilder.
     *
     * <p>{@link ThrowingCallable} is excluded: {@code Reality.checkThatThrownBy(callable)}
     * has no {@code StatementBuilder.that(callable)} equivalent by design.
     * {@link CheckFactory} is also excluded as it is always paired with a second parameter.
     */
    @Test
    void statementBuilder_coversAllRealityCheckThatParameterTypes() {
        Set<Class<?>> realityParamTypes = firstParamTypesOf(Reality.class, "checkThat");
        realityParamTypes.remove(ThrowingCallable.class);
        realityParamTypes.remove(CheckFactory.class);

        Set<Class<?>> builderParamTypes = firstParamTypesOf(StatementBuilder.class, "that");

        Set<Class<?>> missing = new HashSet<>(realityParamTypes);
        missing.removeAll(builderParamTypes);

        assertTrue(missing.isEmpty(),
                "StatementBuilder is missing that() overloads for parameter types: " + missing
                + "\nAdd the missing overloads to StatementBuilder.");
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private static Set<String> methodsStartingWith(Class<?> cls, String... prefixes) {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .filter(name -> Arrays.stream(prefixes).anyMatch(name::startsWith))
                .collect(Collectors.toSet());
    }

    private static Set<Class<?>> firstParamTypesOf(Class<?> cls, String... prefixes) {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> Arrays.stream(prefixes).anyMatch(m.getName()::startsWith))
                .filter(m -> m.getParameterCount() >= 1)
                .map(m -> m.getParameterTypes()[0])
                .collect(Collectors.toCollection(HashSet::new));
    }
}
