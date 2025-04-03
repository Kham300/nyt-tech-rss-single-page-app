package com.nyt.technews.config;

import org.thymeleaf.spring6.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {

    @Bean
    public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver(SpringWebFluxTemplateEngine templateEngine) {
        ThymeleafReactiveViewResolver resolver = new ThymeleafReactiveViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setOrder(1);
        return resolver;
    }

    @Bean
    public SpringWebFluxTemplateEngine templateEngine() {
        SpringWebFluxTemplateEngine engine = new SpringWebFluxTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    @Bean
    public ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        return resolver;
    }

}
