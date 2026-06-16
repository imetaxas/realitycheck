package com.yanimetaxas.realitycheck;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards against entry-point drift. The four public entry-points use different naming
 * conventions ({@code checkThat*} / {@code assertThat*} / {@code that*}), but within
 * the same convention the method sets must stay in sync.
 *
 * <ul>
 *   <li>{@link Reality} and {@link SoftChecks} both use {@code checkThat*} — verified here.
 *   <li>{@link RealityAssertions} uses {@code assertThat*} aliases — verified here against Reality.
 * </ul>
 *
 * If a new {@code checkThat*} method is added to {@link Reality} without also being added
 * to {@link SoftChecks}, this test will fail.
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

    private static Set<String> methodsStartingWith(Class<?> cls, String... prefixes) {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .filter(name -> Arrays.stream(prefixes).anyMatch(name::startsWith))
                .collect(Collectors.toSet());
    }
}
