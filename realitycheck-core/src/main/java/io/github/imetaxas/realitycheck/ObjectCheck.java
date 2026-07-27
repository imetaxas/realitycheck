package io.github.imetaxas.realitycheck;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Fluent assertions for arbitrary objects.
 *
 * @param <T> the type of the value under test
 */
public final class ObjectCheck<T> extends AbstractCheck<ObjectCheck<T>, T> {

    ObjectCheck(T actual, FailureHandler handler) {
        super(actual, handler);
    }

    public ObjectCheck<T> hasToString(String expected) {
        String str = String.valueOf(actual());
        return failureHandler().check(self(), str.equals(expected),
                "expected toString() <%s> but was: <%s>", expected, str);
    }

    public ObjectCheck<T> hasSameHashCodeAs(Object other) {
        int actualHash = actual() == null ? 0 : actual().hashCode();
        int otherHash = other == null ? 0 : other.hashCode();
        return failureHandler().check(self(), actualHash == otherHash,
                "expected hashCode <%d> but was: <%d>", otherHash, actualHash);
    }

    /**
     * Asserts that the actual object has the same first-level field values as {@code expected},
     * using safe shallow reflection (no recursion into nested objects).
     *
     * <h3>Safety guards applied</h3>
     * <ul>
     *   <li>Skips {@code static} and synthetic fields (compiler-generated, e.g. {@code $jacocoData}).
     *   <li>Skips any field named {@code metaClass} (Groovy artefact).</li>
     *   <li>Skips the outer-class back-reference in non-static inner classes
     *       (field name starting with {@code this$}).</li>
     *   <li>Skips the comparison entirely if either object is a JDK proxy or CGLIB/Spring proxy
     *       (detected via {@link Proxy#isProxyClass} or class name containing {@code $$}).
     *   <li>If a field is inaccessible ({@link InaccessibleObjectException} or JPMS module
     *       restriction) the field is silently skipped and a warning is recorded in the failure
     *       message so the caller knows the comparison was partial.</li>
     * </ul>
     *
     * <p>Comparison uses {@link Object#equals} on the field values; it does <em>not</em> recurse.
     *
     * @param expected the object whose field values are expected
     * @return {@code this}
     */
    public ObjectCheck<T> hasSameFieldsAs(T expected) {
        if (!isActualPresent()) return self();
        if (expected == null) {
            failureHandler().fail("hasSameFieldsAs: expected object must not be null");
            return self();
        }

        T act = actual();

        if (isProxy(act) || isProxy(expected)) {
            failureHandler().fail(
                    "hasSameFieldsAs: cannot compare proxy objects via reflection — "
                    + "use a custom equality check instead");
            return self();
        }

        Class<?> type = act.getClass();
        List<String> mismatches = new ArrayList<>();
        List<String> inaccessible = new ArrayList<>();

        for (Field field : type.getDeclaredFields()) {
            if (shouldSkip(field)) continue;

            try {
                field.setAccessible(true);
            } catch (InaccessibleObjectException | SecurityException e) {
                inaccessible.add(field.getName());
                continue;
            }

            Object actualValue;
            Object expectedValue;
            try {
                actualValue   = field.get(act);
                expectedValue = field.get(expected);
            } catch (IllegalAccessException e) {
                inaccessible.add(field.getName());
                continue;
            }

            if (!objectsEqual(actualValue, expectedValue)) {
                mismatches.add(String.format(
                        "  field '%s': expected <%s> but was <%s>",
                        field.getName(), expectedValue, actualValue));
            }
        }

        if (!inaccessible.isEmpty()) {
            mismatches.add("  (skipped inaccessible fields: " + inaccessible + ")");
        }

        if (!mismatches.isEmpty()) {
            failureHandler().fail("field mismatch(es) in <%s>:\n%s",
                    type.getSimpleName(), String.join("\n", mismatches));
        }

        return self();
    }

    private static boolean shouldSkip(Field field) {
        return Modifier.isStatic(field.getModifiers())
                || field.isSynthetic()
                || field.getName().startsWith("this$")
                || "metaClass".equals(field.getName());
    }

    private static boolean isProxy(Object obj) {
        if (obj == null) return false;
        Class<?> cls = obj.getClass();
        return Proxy.isProxyClass(cls) || cls.getName().contains("$$");
    }

    private static boolean objectsEqual(Object a, Object b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}
