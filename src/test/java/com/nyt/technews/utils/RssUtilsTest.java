package com.nyt.technews.utils;

import com.nyt.technews.dto.ArticleDto;
import com.nyt.technews.exception.FeedProcessingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RssUtilsTest {

    @Test
    void testParseXml_ValidXml() throws URISyntaxException, IOException {
        String validXml = new String(
                Files.readAllBytes(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(
                        "rss-news.xml")).toURI())),
                StandardCharsets.UTF_8
        );

        List<ArticleDto> articles = RssUtils.parseXml(validXml);

        assertEquals(1, articles.size());
        assertEquals("Title", articles.get(0).title());
        assertEquals("http://example.com", articles.get(0).url());
        assertEquals("Test description", articles.get(0).summary());
        assertNotNull(articles.get(0).publicationDate());
        assertEquals("http://example.com/image.jpg", articles.get(0).imageUrl());
    }

    @Test
    void testParseXml_EmptyXml() {
        List<ArticleDto> articles = RssUtils.parseXml("");

        assertTrue(articles.isEmpty());
    }

    @Test
    void testParseXml_BlankXml() {
        List<ArticleDto> articles = RssUtils.parseXml("    ");

        assertTrue(articles.isEmpty());
    }

    @Test
    void testParseXml_InvalidXml() {
        String invalidXml = "<rss><channel><item><title>Title</item>";

        assertThrows(FeedProcessingException.class, () -> RssUtils.parseXml(invalidXml));
    }
}
