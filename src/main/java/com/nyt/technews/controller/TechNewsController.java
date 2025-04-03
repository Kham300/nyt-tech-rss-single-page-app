package com.nyt.technews.controller;

import com.nyt.technews.service.NytRssService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TechNewsController {
    private final NytRssService rssService;

    @GetMapping
    public Mono<String> getTechArticles(Model model,
                                        @RequestParam(required = false, defaultValue = "en") String lang) {
        return rssService.fetchTechArticles()
                .doOnNext(articles -> {
                    model.addAttribute("articles", articles);
                    model.addAttribute("currentLang", lang);
                })
                .thenReturn("articles");
    }
}
