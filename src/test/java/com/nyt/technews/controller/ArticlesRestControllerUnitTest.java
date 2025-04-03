package com.nyt.technews.controller;

import com.nyt.technews.dto.ArticleDto;
import com.nyt.technews.exception.FeedProcessingException;
import com.nyt.technews.service.NytRssService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(ArticlesRestController.class)
class ArticlesRestControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private NytRssService rssService;

    @Test
    void getTechArticles_ReturnsCachedArticles() {
        // Mock service layer directly
        when(rssService.fetchTechArticles())
                .thenReturn(Mono.just(List.of(
                        new ArticleDto("Cached Article", "...", "...", Instant.now(), null)
                )));

        webTestClient.get().uri("/api/articles")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.articles[0].title").isEqualTo("Cached Article");
    }

    @Test
    void getTechArticles_Returns500Error() {
        // Mock service layer directly
        when(rssService.fetchTechArticles())
                .thenReturn(Mono.error(new FeedProcessingException("Error Fetching Rss Feed", new RuntimeException())));

        // Verify error response structure
        webTestClient.get().uri("/api/articles")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class)
                .isEqualTo("Error processing feed: Error Fetching Rss Feed");
    }
}