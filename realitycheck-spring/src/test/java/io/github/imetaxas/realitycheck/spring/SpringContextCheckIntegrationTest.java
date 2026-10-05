package io.github.imetaxas.realitycheck.spring;

import static io.github.imetaxas.realitycheck.spring.SpringReality.assertThatContext;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.imetaxas.realitycheck.Reality;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.GenericApplicationContext;

class SpringContextCheckIntegrationTest {

    record NamedService(String name) {}

    @Configuration
    static class PrimaryConfig {
        @Bean
        @Primary
        NamedService primary() {
            return new NamedService("primary");
        }

        @Bean
        NamedService secondary() {
            return new NamedService("secondary");
        }
    }

    @Configuration
    static class OkConfig {
        @Bean
        NamedService service() {
            return new NamedService("ok");
        }
    }

    @Configuration
    static class FailingConfig {
        @Bean
        NamedService broken() {
            throw new IllegalStateException("cannot create");
        }
    }

    @Test
    void bean_usesPrimaryWhenMultipleBeansExist() {
        try (AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(PrimaryConfig.class)) {
            NamedService primary = ctx.getBean(NamedService.class);
            assertDoesNotThrow(
                    () -> assertThatContext(ctx).bean(NamedService.class).isSameAs(primary));
            AssertionError error =
                    assertThrows(
                            AssertionError.class,
                            () -> assertThatContext(ctx).hasSingleBean(NamedService.class));
            assertTrue(error.getMessage().contains("found 2"));
        }
    }

    @Test
    void hasFailed_whenConfigurableContextNeverRefreshed() {
        GenericApplicationContext ctx = new GenericApplicationContext();
        try {
            assertDoesNotThrow(() -> assertThatContext(ctx).hasFailed());
            AssertionError error =
                    assertThrows(
                            AssertionError.class, () -> assertThatContext(ctx).hasNotFailed());
            assertTrue(error.getMessage().contains("not active"));
            AssertionError missingThrowable =
                    assertThrows(AssertionError.class, () -> assertThatContext(ctx).failure());
            assertTrue(missingThrowable.getMessage().contains("getStartupFailure"));
        } finally {
            ctx.close();
        }
    }

    @Test
    void hasNotFailed_whenConfigurableContextIsActive() {
        try (AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(OkConfig.class)) {
            assertDoesNotThrow(() -> assertThatContext(ctx).hasNotFailed());
        }
    }

    @Test
    void runner_hasNotFailedAndBean() {
        new ApplicationContextRunner()
                .withUserConfiguration(OkConfig.class)
                .run(
                        context -> {
                            assertThatContext(context).hasNotFailed();
                            assertThatContext(context)
                                    .bean(NamedService.class)
                                    .isInstanceOf(NamedService.class);
                        });
    }

    @Test
    void runner_hasFailedAndFailure() {
        new ApplicationContextRunner()
                .withUserConfiguration(FailingConfig.class)
                .run(
                        context -> {
                            assertThatContext(context).hasFailed();
                            assertThatContext(context)
                                    .failure()
                                    .isInstanceOf(BeanCreationException.class)
                                    .hasMessageContaining("cannot create");
                        });
    }

    @Test
    void checkThatContext_withSoftChecks_collectsFailures() {
        try (AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(OkConfig.class)) {
            AssertionError error =
                    assertThrows(
                            AssertionError.class,
                            () ->
                                    Reality.checkAll(
                                            softly -> {
                                                SpringReality.checkThatContext(ctx, softly)
                                                        .doesNotHaveBean(NamedService.class);
                                                softly.checkThat("x").isEmpty();
                                            }));
            assertTrue(error.getMessage().contains("expected no bean of type")
                    || error.getSuppressed().length >= 1);
        }
    }

    @Test
    void checkThatContext_withSoftChecks_allPass() {
        try (AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(OkConfig.class)) {
            assertDoesNotThrow(
                    () ->
                            Reality.checkAll(
                                    softly ->
                                            SpringReality.assertThatContext(ctx, softly)
                                                    .hasSingleBean(NamedService.class)));
        }
    }
}
