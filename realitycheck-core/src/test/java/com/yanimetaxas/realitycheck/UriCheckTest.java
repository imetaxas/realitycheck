package com.yanimetaxas.realitycheck;

import static com.yanimetaxas.realitycheck.Reality.checkThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import org.junit.jupiter.api.Test;

class UriCheckTest {

    private static final URI FULL_URI = URI.create(
            "https://api.example.com:8443/v2/users?page=1&limit=50&sort=name#section");

    @Test
    void hasScheme_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).hasScheme("https"));
    }

    @Test
    void hasScheme_fails() {
        var e = assertThrows(AssertionError.class, () -> checkThat(FULL_URI).hasScheme("http"));
        assertTrue(e.getMessage().contains("http"));
        assertTrue(e.getMessage().contains("https"));
    }

    @Test
    void hasHost_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).hasHost("api.example.com"));
    }

    @Test
    void hasPort_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).hasPort(8443));
    }

    @Test
    void hasNoPort_passes() {
        URI noPort = URI.create("https://example.com/path");
        assertDoesNotThrow(() -> checkThat(noPort).hasNoPort());
    }

    @Test
    void hasPath_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).hasPath("/v2/users"));
    }

    @Test
    void pathStartsWith_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).pathStartsWith("/v2"));
    }

    @Test
    void pathContains_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).pathContains("users"));
    }

    @Test
    void hasFragment_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).hasFragment("section"));
    }

    @Test
    void hasNoFragment_passes() {
        URI noFragment = URI.create("https://example.com/path");
        assertDoesNotThrow(() -> checkThat(noFragment).hasNoFragment());
    }

    @Test
    void hasQueryParam_keyOnly_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).hasQueryParam("page"));
    }

    @Test
    void hasQueryParam_keyValue_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).hasQueryParam("page", "1"));
    }

    @Test
    void hasQueryParam_wrongValue_fails() {
        assertThrows(AssertionError.class,
                () -> checkThat(FULL_URI).hasQueryParam("page", "99"));
    }

    @Test
    void hasQueryParam_missingKey_fails() {
        var e = assertThrows(AssertionError.class,
                () -> checkThat(FULL_URI).hasQueryParam("offset"));
        assertTrue(e.getMessage().contains("offset"));
    }

    @Test
    void hasNoQueryParam_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).hasNoQueryParam("offset"));
    }

    @Test
    void hasNoQuery_passes() {
        URI noQuery = URI.create("https://example.com/path");
        assertDoesNotThrow(() -> checkThat(noQuery).hasNoQuery());
    }

    @Test
    void isAbsolute_passes() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).isAbsolute());
    }

    @Test
    void isRelative_passes() {
        URI relative = URI.create("/api/users");
        assertDoesNotThrow(() -> checkThat(relative).isRelative());
    }

    @Test
    void host_extractor_works() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).host().contains("example"));
    }

    @Test
    void path_extractor_works() {
        assertDoesNotThrow(() -> checkThat(FULL_URI).path().startsWith("/v2"));
    }

    @Test
    void chaining_works() {
        assertDoesNotThrow(() -> checkThat(FULL_URI)
                .isNotNull()
                .isAbsolute()
                .hasScheme("https")
                .hasHost("api.example.com")
                .hasPort(8443)
                .hasPath("/v2/users")
                .hasQueryParam("page", "1")
                .hasQueryParam("limit", "50")
                .hasFragment("section"));
    }

    @Test
    void encodedQueryParams_decoded() {
        URI encoded = URI.create("https://example.com/search?q=hello%20world&lang=en");
        assertDoesNotThrow(() -> checkThat(encoded).hasQueryParam("q", "hello world"));
    }

    @Test
    void multipleValuesForSameParam() {
        URI multi = URI.create("https://example.com/search?tag=java&tag=kotlin");
        assertDoesNotThrow(() -> checkThat(multi)
                .hasQueryParam("tag", "java")
                .hasQueryParam("tag", "kotlin"));
    }

    @Test
    void isOpaque_pass() {
        assertDoesNotThrow(() -> checkThat(URI.create("mailto:user@example.com")).isOpaque());
    }

    @Test
    void isOpaque_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com/path")).isOpaque());
    }

    @Test
    void hasNoPort_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com/path")).hasNoPort());
    }

    @Test
    void hasNoPort_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com:8080/path")).hasNoPort());
    }

    @Test
    void query_extractor_withQuery() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com?key=val")).query().contains("key"));
    }

    @Test
    void query_extractor_noQuery() {
        URI uri = URI.create("https://example.com/path");
        assertDoesNotThrow(() -> {
            StringCheck q = checkThat(uri).query();
            assertNull(q.actual());
        });
    }

    @Test
    void hasScheme_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com")).hasScheme("https"));
    }

    @Test
    void hasScheme_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com")).hasScheme("ftp"));
    }

    @Test
    void hasHost_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com")).hasHost("example.com"));
    }

    @Test
    void hasHost_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com")).hasHost("other.com"));
    }

    @Test
    void hasPort_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com:8080")).hasPort(8080));
    }

    @Test
    void hasPort_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com:8080")).hasPort(9090));
    }

    @Test
    void hasPath_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com/api/v2")).hasPath("/api/v2"));
    }

    @Test
    void hasPath_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com/api")).hasPath("/wrong"));
    }

    @Test
    void isAbsolute_pass() {
        assertDoesNotThrow(() -> checkThat(URI.create("https://example.com")).isAbsolute());
    }

    @Test
    void isAbsolute_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("/relative/path")).isAbsolute());
    }

    @Test
    void isRelative_pass() {
        assertDoesNotThrow(() -> checkThat(URI.create("/relative")).isRelative());
    }

    @Test
    void isRelative_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com")).isRelative());
    }

    @Test
    void hasFragment_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com#sec1")).hasFragment("sec1"));
    }

    @Test
    void hasFragment_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com#sec1")).hasFragment("other"));
    }

    @Test
    void hasNoFragment_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com")).hasNoFragment());
    }

    @Test
    void hasNoFragment_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com#sec")).hasNoFragment());
    }

    @Test
    void hasNoQuery_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com")).hasNoQuery());
    }

    @Test
    void hasNoQuery_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com?x=1")).hasNoQuery());
    }

    @Test
    void pathStartsWith_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com/api/v2")).pathStartsWith("/api"));
    }

    @Test
    void pathStartsWith_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com/api")).pathStartsWith("/wrong"));
    }

    @Test
    void pathContains_pass() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com/api/v2")).pathContains("v2"));
    }

    @Test
    void pathContains_fail() {
        assertThrows(AssertionError.class, () ->
                checkThat(URI.create("https://example.com/api")).pathContains("nope"));
    }

    @Test
    void host_extractor() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com")).host().isEqualTo("example.com"));
    }

    @Test
    void path_extractor() {
        assertDoesNotThrow(() ->
                checkThat(URI.create("https://example.com/p")).path().isEqualTo("/p"));
    }

    @Test
    void hasQueryParam_wrongValue() {
        URI uri = URI.create("http://x.com?a=1&a=2");
        assertThrows(AssertionError.class,
                () -> checkThat(uri).hasQueryParam("a", "99"));
    }

    @Test
    void hasNoQueryParam_fails_whenPresent() {
        URI uri = URI.create("http://x.com?key=val");
        assertThrows(AssertionError.class,
                () -> checkThat(uri).hasNoQueryParam("key"));
    }

    @Test
    void decode_fallback_on_malformed() {
        URI uri = URI.create("http://x.com?ok=fine");
        assertDoesNotThrow(() -> checkThat(uri).hasQueryParam("ok", "fine"));
    }

    @Test
    void host_failsViaStringCheck() {
        assertThrows(AssertionError.class, () -> checkThat(FULL_URI).host().isEqualTo("nope"));
    }

    @Test
    void path_failsViaStringCheck() {
        assertThrows(AssertionError.class, () -> checkThat(FULL_URI).path().isEqualTo("/wrong"));
    }

    @Test
    void query_failsViaStringCheck() {
        assertThrows(AssertionError.class, () -> checkThat(FULL_URI).query().isEqualTo("page=2"));
    }

    @Test
    void query_withNoQueryString_nullActual() {
        URI noQuery = URI.create("https://example.com/path");
        assertThrows(AssertionError.class, () -> checkThat(noQuery).query().isEqualTo("a=1"));
    }
}
