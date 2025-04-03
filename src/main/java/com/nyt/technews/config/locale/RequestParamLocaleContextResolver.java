package com.nyt.technews.config.locale;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;

import java.util.List;
import java.util.Locale;

public class RequestParamLocaleContextResolver implements LocaleContextResolver {

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        List<String> referLang = exchange.getRequest().getQueryParams().get("lang");

        if (CollectionUtils.isEmpty(referLang)) {
            return new SimpleLocaleContext(DEFAULT_LOCALE);
        }

        String lang = referLang.get(0);
        Locale targetLocale = isValidLanguageTag(lang) ? Locale.forLanguageTag(lang) : DEFAULT_LOCALE;

        return new SimpleLocaleContext(targetLocale);
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
        if (localeContext != null && localeContext.getLocale() != null) {
            exchange.getAttributes().put("locale", localeContext.getLocale());
        }
    }

    /**
     * Checks if the language tag is valid.
     */
    private boolean isValidLanguageTag(String lang) {
        return lang != null && !lang.isBlank() && lang.matches("^[a-zA-Z]{2,3}(-[a-zA-Z]{2,3})?$");
    }
}
