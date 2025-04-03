package com.nyt.technews.dto;

import java.time.Instant;

public record ArticleDto(
        String title,
        String url,
        String summary,
        Instant publicationDate,
        String imageUrl
) {}
