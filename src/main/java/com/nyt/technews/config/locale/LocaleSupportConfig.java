package com.nyt.technews.config.locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.server.i18n.LocaleContextResolver;

@Configuration
public class LocaleSupportConfig extends DelegatingWebFluxConfiguration {
    @Override
    protected LocaleContextResolver createLocaleContextResolver() {
        return new RequestParamLocaleContextResolver();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
