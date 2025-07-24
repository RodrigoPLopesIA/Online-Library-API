package br.com.rodrigo.onlinelibraryapi.integration;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

        public static void startContainers() {
            Startables.deepStart(Stream.of(container)).join();
        }

        public static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", container.getJdbcUrl(),
                    "spring.datasource.username", container.getUsername(),
                    "spring.datasource.password", container.getPassword());
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource mapPropertySource = new MapPropertySource("testcontainers",
                    (Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(mapPropertySource);
        }

    }
}