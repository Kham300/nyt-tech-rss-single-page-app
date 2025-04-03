package com.nyt.technews.service;

import com.nyt.technews.dto.ArticleDto;
import com.nyt.technews.utils.RssUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NytRssService {

    private final NytRssClient nytRssClient;

    @Cacheable(value = "articles", unless = "#result.isEmpty()")
    public Mono<List<ArticleDto>> fetchTechArticles() {
        return nytRssClient.fetchRssXml()
                .map(RssUtils::parseXml)
                .defaultIfEmpty(List.of())
                .doOnSuccess(articles -> log.info("Parsed {} articles", articles.size()))
                .doOnError(error -> log.error("Failed to parse RSS feed", error));
    }

}
