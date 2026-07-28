/**
 * Reality Check — fluent assertion library for Java 17+.
 *
 * <h2>Quick start for humans and AI agents</h2>
 *
 * <p>There is one import and one method name to learn:
 *
 * <pre>{@code
 * import static io.github.imetaxas.realitycheck.RealityAssertions.*;
 *
 * assertThat("hello").isNotEmpty().startsWith("he");
 * assertThat(42).isPositive().isBetween(1, 100);
 * assertThat(list).hasSize(3).contains("item");
 * assertThat(uri).hasScheme("https").hasHost("example.com");
 * assertThat(Status.ACTIVE).hasName("ACTIVE");   // routes to EnumCheck automatically
 * assertThat(myPojo).hasSameFieldsAs(expected);  // shallow field comparison
 * }</pre>
 *
 * <h2>Soft assertions (collect all failures)</h2>
 *
 * <pre>{@code
 * assertAll(softly -> {
 *     softly.assertThat(name).as("name").isNotEmpty();
 *     softly.assertThat(age).as("age").isPositive();
 * });
 * }</pre>
 *
 * <h2>Custom message on failure</h2>
 *
 * <pre>{@code
 * assertWithMessage("User must be active").that(user.getStatus()).isEqualTo(Status.ACTIVE);
 * }</pre>
 *
 * <h2>Assertion label (shown in failure message)</h2>
 *
 * <pre>{@code
 * assertThat(value).as("user email").contains("@");
 * // failure: [user email] expected string to contain <@> but was: <invalid>
 * }</pre>
 *
 * <h2>Custom check types (3 lines)</h2>
 *
 * <pre>{@code
 * record MoneyCheck(Money actual, FailureHandler failureHandler)
 *         implements Check<MoneyCheck, Money> {
 *     public MoneyCheck self() { return this; }
 *     public MoneyCheck hasCurrency(String code) {
 *         return failureHandler.check(self(), actual.getCurrency().equals(code),
 *             "expected currency <%s> but was <%s>", code, actual.getCurrency());
 *     }
 * }
 * assertThat(payment, MoneyCheck::new).hasCurrency("USD");
 * }</pre>
 *
 * <h2>Entry-point classes</h2>
 * <ul>
 *   <li>{@link io.github.imetaxas.realitycheck.RealityAssertions} —
 *       {@code assertThat()} naming; recommended for new code and AssertJ/Truth migrations.</li>
 *   <li>{@link io.github.imetaxas.realitycheck.Reality} —
 *       {@code checkThat()} naming; identical behaviour.</li>
 *   <li>{@link io.github.imetaxas.realitycheck.SoftChecks} —
 *       soft-assertion context; used inside {@code assertAll(softly -> ...)}.</li>
 * </ul>
 *
 * <h2>Type routing rules (relevant for AI agents)</h2>
 * <ul>
 *   <li>{@code String} → {@link io.github.imetaxas.realitycheck.StringCheck}</li>
 *   <li>{@code int / long / double / float} →
 *       {@link io.github.imetaxas.realitycheck.NumberCheck}</li>
 *   <li>{@code boolean} → {@link io.github.imetaxas.realitycheck.BooleanCheck}</li>
 *   <li>{@code Collection<T>} → {@link io.github.imetaxas.realitycheck.CollectionCheck}</li>
 *   <li>{@code Map<K,V>} → {@link io.github.imetaxas.realitycheck.MapCheck}</li>
 *   <li>{@code Optional<T>} → {@link io.github.imetaxas.realitycheck.OptionalCheck}</li>
 *   <li>{@code Path / File} → {@link io.github.imetaxas.realitycheck.FileCheck}</li>
 *   <li>{@code URI} → {@link io.github.imetaxas.realitycheck.UriCheck}</li>
 *   <li>{@code Instant / LocalDate / LocalDateTime / Duration / ZonedDateTime / OffsetDateTime}
 *       → dedicated date-time checks</li>
 *   <li>{@code int[] / long[] / double[] / float[] / byte[]}
 *       → dedicated primitive array checks</li>
 *   <li>{@code E extends Enum<E>} →
 *       {@link io.github.imetaxas.realitycheck.EnumCheck} (routed automatically)</li>
 *   <li>Any other reference type → {@link io.github.imetaxas.realitycheck.ObjectCheck}
 *       (generic fallback)</li>
 * </ul>
 *
 * @see io.github.imetaxas.realitycheck.RealityAssertions
 * @see io.github.imetaxas.realitycheck.Reality
 * @see io.github.imetaxas.realitycheck.SoftChecks
 */
package io.github.imetaxas.realitycheck;
