package com.nyt.technews.service;

import com.nyt.technews.dto.ArticleDto;
import com.nyt.technews.exception.FeedProcessingException;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NytRssService {
    private final NytRssClient nytRssClient;

    @Cacheable("articles")
    public Mono<List<ArticleDto>> fetchTechArticles() {
        return nytRssClient.fetchRssXml()
                .map(this::parseXml)
                .doOnSuccess(articles -> log.info("Parsed {} articles", articles.size()))
                .doOnError(error -> log.error("Failed to parse RSS feed", error));
    }

    private List<ArticleDto> parseXml(String xml) {
        try {
            SyndFeed feed = new SyndFeedInput().build(new StringReader(xml));
            return feed.getEntries().stream()
                    .map(this::mapToArticle)
                    .toList();
        } catch (Exception e) {
            log.error("Error parsing RSS XML", e);
            throw new FeedProcessingException("Error parsing RSS feed", e);
        }
    }

    private ArticleDto mapToArticle(SyndEntry entry) {
        return new ArticleDto(
                entry.getTitle(),
                entry.getLink(),
                entry.getDescription().getValue(),
                entry.getPublishedDate().toInstant(),
                extractImageUrl(entry)
        );
    }

    private String extractImageUrl(SyndEntry entry) {
        return entry.getForeignMarkup().stream()
                .filter(e -> "content".equals(e.getName()) && "http://search.yahoo.com/mrss/".equals(e.getNamespaceURI()))
                .map(e -> e.getAttributeValue("url"))
                .findFirst()
                .orElse(null); // Default to null if no image is found
    }
}
