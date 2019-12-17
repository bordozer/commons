package com.bordozer.commons.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class CommonsWebConfig {

    @Bean
    public WebLogger webLogger() {
        return new WebLogger();
    }

    @Bean
    @Order(1)
    public FilterRegistrationBean<RequestIdFilter> requestIdFilter() {
        final FilterRegistrationBean<RequestIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestIdFilter());
        return registrationBean;
    }

    @Bean
    @Order(2)
    public FilterRegistrationBean<RequestLogFilter> requestLogFilter(
            final WebLogger webLogger,
            @Value("${application.properties.logging.logRequestForUrls:}") List<String> requestLogUrls,
            @Value("${application.properties.logging.logRequest:true}") boolean logRequest,
            @Value("${application.properties.logging.logResponse:true}") boolean logResponse
    ) {
        final FilterRegistrationBean<RequestLogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLogFilter(logRequest, logResponse, webLogger));
        registrationBean.setUrlPatterns(requestLogUrls);
        return registrationBean;
    }
}
