package com.nyt.technews.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final NytApiProperties properties;

    @Bean
    public WebClient nytWebClient(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create(connectionProvider())
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        Math.toIntExact(properties.getTimeouts().getConnect().toMillis()))
                .responseTimeout(properties.getTimeouts().getRead())
                .doOnConnected(conn -> conn
                        .addHandler(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                        .addHandler(new WriteTimeoutHandler(10)));

        return webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // 16MB buffer
                .build();
    }

    private ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("nyt-connection-pool")
                .maxConnections(properties.getConnectionPool().getMaxConnections())
                .pendingAcquireTimeout(properties.getConnectionPool().getAcquireTimeout())
                .maxIdleTime(properties.getConnectionPool().getIdleTimeout())
                .build();
    }
}
