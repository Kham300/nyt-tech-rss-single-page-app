package com.nyt.technews.dto;

import java.util.List;

public record TechArticlesResponse(List<ArticleDto> articles, int size) {
    public TechArticlesResponse(List<ArticleDto> articles) {
        this(articles, articles != null ? articles.size() : 0);
    }
}
