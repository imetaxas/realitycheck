package io.github.imetaxas.realitycheck;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Fluent assertions for {@link URI} values. Parses and inspects individual URI components
 * without external dependencies.
 *
 * <pre>{@code
 * checkThat(uri)
 *     .hasScheme("https")
 *     .hasHost("api.example.com")
 *     .hasPath("/v2/users")
 *     .hasQueryParam("page", "1");
 *
 * checkThat(URI.create(url)).isAbsolute().hasPort(443);
 * }</pre>
 */
public final class UriCheck extends AbstractCheck<UriCheck, URI> {

    UriCheck(URI actual, FailureHandler handler) {
        super(actual, handler);
    }

    public UriCheck hasScheme(String expected) {
        return failureHandler().check(self(), Objects.equals(expected, actual().getScheme()),
                "expected scheme <%s> but was: <%s> in URI <%s>",
                expected, actual().getScheme(), actual());
    }

    public UriCheck hasHost(String expected) {
        return failureHandler().check(self(), Objects.equals(expected, actual().getHost()),
                "expected host <%s> but was: <%s> in URI <%s>",
                expected, actual().getHost(), actual());
    }

    public UriCheck hasPort(int expected) {
        return failureHandler().check(self(), actual().getPort() == expected,
                "expected port <%d> but was: <%d> in URI <%s>",
                expected, actual().getPort(), actual());
    }

    public UriCheck hasNoPort() {
        return failureHandler().check(self(), actual().getPort() == -1,
                "expected no explicit port but was: <%d> in URI <%s>",
                actual().getPort(), actual());
    }

    public UriCheck hasPath(String expected) {
        return failureHandler().check(self(), Objects.equals(expected, actual().getPath()),
                "expected path <%s> but was: <%s> in URI <%s>",
                expected, actual().getPath(), actual());
    }

    public UriCheck pathStartsWith(String prefix) {
        String path = actual().getPath();
        return failureHandler().check(self(), path != null && path.startsWith(prefix),
                "expected path starting with <%s> but was: <%s> in URI <%s>",
                prefix, path, actual());
    }

    public UriCheck pathContains(String substring) {
        String path = actual().getPath();
        return failureHandler().check(self(), path != null && path.contains(substring),
                "expected path containing <%s> but was: <%s> in URI <%s>",
                substring, path, actual());
    }

    public UriCheck hasFragment(String expected) {
        return failureHandler().check(self(), Objects.equals(expected, actual().getFragment()),
                "expected fragment <%s> but was: <%s> in URI <%s>",
                expected, actual().getFragment(), actual());
    }

    public UriCheck hasNoFragment() {
        return failureHandler().check(self(), actual().getFragment() == null,
                "expected no fragment but was: <%s> in URI <%s>",
                actual().getFragment(), actual());
    }

    public UriCheck hasQueryParam(String key) {
        Map<String, List<String>> params = parseQuery();
        return failureHandler().check(self(), params.containsKey(key),
                "expected query param <%s> but params were: %s in URI <%s>",
                key, params.keySet(), actual());
    }

    public UriCheck hasQueryParam(String key, String expectedValue) {
        Map<String, List<String>> params = parseQuery();
        if (!params.containsKey(key)) {
            failureHandler().fail("expected query param <%s=%s> but params were: %s in URI <%s>",
                    key, expectedValue, params.keySet(), actual());
        } else if (!params.get(key).contains(expectedValue)) {
            failureHandler().fail("expected query param <%s=%s> but values were: %s in URI <%s>",
                    key, expectedValue, params.get(key), actual());
        }
        return self();
    }

    public UriCheck hasNoQueryParam(String key) {
        Map<String, List<String>> params = parseQuery();
        return failureHandler().check(self(), !params.containsKey(key),
                "expected no query param <%s> but it was present with value(s): %s",
                key, params.get(key));
    }

    public UriCheck hasNoQuery() {
        return failureHandler().check(self(), actual().getQuery() == null,
                "expected no query string but was: <%s> in URI <%s>",
                actual().getQuery(), actual());
    }

    public UriCheck isAbsolute() {
        return failureHandler().check(self(), actual().isAbsolute(),
                "expected an absolute URI but was: <%s>", actual());
    }

    public UriCheck isRelative() {
        return failureHandler().check(self(), !actual().isAbsolute(),
                "expected a relative URI but was: <%s>", actual());
    }

    public UriCheck isOpaque() {
        return failureHandler().check(self(), actual().isOpaque(),
                "expected an opaque URI but was: <%s>", actual());
    }

    /** Extracts the host as a {@link StringCheck} for further assertions. */
    public StringCheck host() {
        return new StringCheck(actual().getHost(), failureHandler());
    }

    /** Extracts the path as a {@link StringCheck} for further assertions. */
    public StringCheck path() {
        return new StringCheck(actual().getPath(), failureHandler());
    }

    /** Extracts the query string as a {@link StringCheck} for further assertions. */
    public StringCheck query() {
        return new StringCheck(actual().getQuery(), failureHandler());
    }

    private Map<String, List<String>> parseQuery() {
        Map<String, List<String>> params = new LinkedHashMap<>();
        String query = actual().getRawQuery();
        if (query == null || query.isEmpty()) return params;

        for (String pair : query.split("&")) {
            int eq = pair.indexOf('=');
            String key = eq >= 0 ? decode(pair.substring(0, eq)) : decode(pair);
            String value = eq >= 0 ? decode(pair.substring(eq + 1)) : "";
            params.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
        return params;
    }

    private static String decode(String encoded) {
        try {
            return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return encoded;
        }
    }
}
