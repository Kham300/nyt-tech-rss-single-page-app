package com.nyt.technews.service;

import com.nyt.technews.config.properties.NytApiProperties;
import com.nyt.technews.config.properties.RetryConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class NytRssClient {
    private final WebClient webClient;
    private final RetryConfig retryConfig;
    private final NytApiProperties properties;

    public Mono<String> fetchRssXml() {
        return webClient.get()
                .uri(properties.getRssPath())
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(retryConfig.getMaxAttempts(), retryConfig.getInitialInterval())
                                   .maxBackoff(retryConfig.getMaxInterval())
                                   .doBeforeRetry(retrySignal ->
                                                          log.warn(
                                                                  "Retrying RSS fetch due to error: {}",
                                                                  retrySignal.failure().getMessage()
                                                          )
                                   )
                )
                .doOnSubscribe(__ -> log.debug("Fetching RSS feed from {}", properties.getRssPath()))
                .doOnSuccess(__ -> log.info("Successfully fetched RSS feed"))
                .doOnError(e -> log.error("Failed to fetch RSS feed: {}", e.getMessage()))
                .doOnRequest(n -> log.trace("Requesting RSS feed with demand: {}", n))
                .doOnNext(body -> log.trace("Received response body: {}", body));
    }
}
