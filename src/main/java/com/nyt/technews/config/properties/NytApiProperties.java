package com.nyt.technews.config.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "nyt.api")
public class NytApiProperties {
    private String baseUrl;
    private String rssPath;
    private Timeouts timeouts = new Timeouts();
    private ConnectionPool connectionPool = new ConnectionPool();

    @Data
    public static class Timeouts {
        @DurationUnit(ChronoUnit.MILLIS)
        private Duration connect = Duration.ofSeconds(5);
        @DurationUnit(ChronoUnit.MILLIS)
        private Duration read = Duration.ofSeconds(10);
        @DurationUnit(ChronoUnit.MILLIS)
        private Duration write = Duration.ofSeconds(10);

    }

    @Data
    public static class ConnectionPool {
        private int maxConnections = 100;
        @DurationUnit(ChronoUnit.MILLIS)
        private Duration acquireTimeout = Duration.ofSeconds(5);
        @DurationUnit(ChronoUnit.MILLIS)
        private Duration idleTimeout = Duration.ofSeconds(30);

    }
}
