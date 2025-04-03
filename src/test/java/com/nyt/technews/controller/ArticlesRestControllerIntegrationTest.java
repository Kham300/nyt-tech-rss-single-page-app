package com.nyt.technews.controller;

import com.nyt.technews.dto.TechArticlesResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ArticlesRestControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static MockWebServer mockWebServer;
    private static final String RSS_PATH = "/services/xml/rss/nyt/Technology.xml";

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("nyt.api.base-url", () -> mockWebServer.url("/").toString());
        registry.add("nyt.api.rss-path", () -> RSS_PATH);
    }

    @Test
    void getTechArticles_ReturnsValidArticles() {
        // Arrange
        String mockRssResponse = """
                <rss version="2.0">
                    <channel>
                        <title>NYT Technology</title>
                        <item>
                            <title>Test Article 1</title>
                            <link>https://example.com/article1</link>
                            <description>First test article summary</description>
                            <pubDate>Wed, 03 Apr 2024 12:00:00 GMT</pubDate>
                        </item>
                    </channel>
                </rss>
                """;

        mockWebServer.enqueue(new MockResponse()
                                      .setResponseCode(200)
                                      .setHeader("Content-Type", "application/xml")
                                      .setBody(mockRssResponse));

        // Act & Assert
        webTestClient.get().uri("/api/articles")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TechArticlesResponse.class)
                .value(response -> {
                    assertThat(response.articles()).hasSize(1);
                    assertThat(response.articles().get(0).title()).isEqualTo("Test Article 1");
                    assertThat(response.articles().get(0).url()).isEqualTo("https://example.com/article1");
                });
    }

}
