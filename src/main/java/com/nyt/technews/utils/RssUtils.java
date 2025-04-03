package com.nyt.technews.utils;

import com.nyt.technews.dto.ArticleDto;
import com.nyt.technews.exception.FeedProcessingException;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

import java.io.StringReader;
import java.util.List;

public final class RssUtils {
    public static final String ATTNAME_URL = "url";
    public static final String ELEMENT_CONTENT = "content";
    public static final String YAHOO_COM_MRSS = "http://search.yahoo.com/mrss/";

    private RssUtils() {}

    public static List<ArticleDto> parseXml(String xml) {
        if (xml.isBlank()) {
            return List.of();
        }

        try {
            SyndFeed feed = new SyndFeedInput().build(new StringReader(xml));
            return feed.getEntries().stream()
                    .map(RssUtils::mapToArticle)
                    .toList();
        } catch (Exception e) {
            throw new FeedProcessingException("Error parsing RSS feed", e);
        }
    }

    public static ArticleDto mapToArticle(SyndEntry entry) {
        return new ArticleDto(
                entry.getTitle(),
                entry.getLink(),
                entry.getDescription().getValue(),
                entry.getPublishedDate().toInstant(),
                extractImageUrl(entry)
        );
    }

    public static String extractImageUrl(SyndEntry entry) {
        return entry.getForeignMarkup().stream()
                .filter(e -> ELEMENT_CONTENT.equals(e.getName()) &&
                        YAHOO_COM_MRSS.equals(e.getNamespaceURI()))
                .map(e -> e.getAttributeValue(ATTNAME_URL))
                .findFirst()
                .orElse(null);
    }
}
