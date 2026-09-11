package io.github.imetaxas.realitycheck;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards against entry-point drift. All four public entry-points must stay in sync
 * whenever a new type overload is added.
 *
 * <ul>
 *   <li>{@link Reality} and {@link SoftChecks} — complete {@code checkThat*} signatures must match.
 *   <li>{@link Reality} and {@link RealityAssertions} — complete {@code checkThat*}/{@code
 *       assertThat*} signatures must match.
 *   <li>{@link Reality} and {@link StatementBuilder} — complete {@code checkThat*}/{@code that*}
 *       signatures must match, except {@link Reality#checkThatThrownBy(ThrowingCallable)}.
 * </ul>
 */
class EntryPointDriftTest {

    @Test
    void softChecks_exposesAllCheckMethodsFromReality() {
        assertNoMissingSignatures(
                signatures(Reality.class, "checkThat", "checkThat"),
                signatures(SoftChecks.class, "checkThat", "checkThat"),
                "SoftChecks");
    }

    @Test
    void realityAssertions_exposesAllAssertThatMethodsFromReality() {
        assertNoMissingSignatures(
                signatures(Reality.class, "checkThat", "assertThat"),
                signatures(RealityAssertions.class, "assertThat", "assertThat"),
                "RealityAssertions");
    }

    @Test
    void statementBuilder_exposesAllSupportedRealitySignatures() {
        Set<Signature> expected = signatures(Reality.class, "checkThat", "that");
        expected.removeIf(signature -> signature.name().equals("thatThrownBy"));

        assertNoMissingSignatures(
                expected,
                signatures(StatementBuilder.class, "that", "that"),
                "StatementBuilder");
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private static Set<Signature> signatures(
            Class<?> type, String sourcePrefix, String normalizedPrefix) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(method -> method.getName().startsWith(sourcePrefix))
                .map(method -> new Signature(
                        normalizedPrefix + method.getName().substring(sourcePrefix.length()),
                        List.of(method.getParameterTypes()),
                        method.getReturnType()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    private static void assertNoMissingSignatures(
            Set<Signature> expected, Set<Signature> actual, String entryPoint) {
        Set<Signature> missing = new HashSet<>(expected);
        missing.removeAll(actual);
        assertTrue(
                missing.isEmpty(),
                () -> entryPoint + " is missing entry-point signatures: " + missing);
    }

    private record Signature(String name, List<Class<?>> parameterTypes, Class<?> returnType) {}
}
