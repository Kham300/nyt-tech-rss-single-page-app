package com.nyt.technews.config.properties;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "nyt.api.retry")
public class RetryConfig {
    private int maxAttempts = 3;
    private Duration initialInterval = Duration.ofSeconds(1);
    private Duration maxInterval = Duration.ofSeconds(5);
}