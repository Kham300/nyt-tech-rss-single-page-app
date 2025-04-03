package com.nyt.technews.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Configuration
public class WebFluxConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource msgSrc = new ReloadableResourceBundleMessageSource();
        msgSrc.setBasename("i18n/messages");
        msgSrc.setDefaultEncoding("UTF-8");
        return msgSrc;
    }

    // LocaleResolver that stores locale in WebSession
    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleResolver();
    }

    @Bean
    public WebFilter localeChangeInterceptor() {
        return (exchange, chain) -> {
            String lang = exchange.getRequest().getQueryParams().getFirst("lang");
            if (lang != null) {
                WebSession session = exchange.getSession().block();
                if (session != null) {
                    // Set locale in session
                    session.getAttributes().put("lang", lang);
                }
            }
            return chain.filter(exchange);
        };
    }

    // Custom LocaleResolver
    public static class LocaleResolver implements WebFilter {
        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
          return exchange.getSession()
                   .map(session -> {
                       if (session != null) {
                           String lang = (String) session.getAttributes().getOrDefault("lang", "en");
                           // Logic for setting the locale in the exchange's response
                           exchange.getResponse().getHeaders().add("Content-Language", lang);
                       }
                       return chain.filter(exchange);
                   }).then();
        }
    }
}
