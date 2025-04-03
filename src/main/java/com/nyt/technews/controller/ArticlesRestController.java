package com.nyt.technews.controller;

import com.nyt.technews.dto.TechArticlesResponse;
import com.nyt.technews.service.NytRssService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticlesRestController {
    private final NytRssService rssService;

    @GetMapping()
    public Mono<TechArticlesResponse> getTechArticles() {
        return rssService.fetchTechArticles()
                .map(TechArticlesResponse::new);
    }

}
