package com.xuchen.project.api.config;

import com.xuchen.project.api.delay.fallback.WorkClientFallback;
import com.xuchen.project.api.security.fallback.SecurityClientFallback;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

@Configuration
public class FeignConfig {

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    @Bean
    public WorkClientFallback workClientFallback() {
        return new WorkClientFallback();
    }

    @Bean
    public SecurityClientFallback securityClientFallback() {
        return new SecurityClientFallback();
    }
}
