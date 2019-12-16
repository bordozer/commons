package com.bordozer.commons.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
public class CommonsWebConfig {

    @Value("${application.properties.logging.logRequestForUrls}")
    private List<String> requestLogUrls = newArrayList();
    @Value("${application.properties.logging.logRequest}")
    private boolean logRequest = true;
    @Value("${application.properties.logging.logResponse}")
    private boolean logResponse = true;

    @Bean
    @Order(1)
    public FilterRegistrationBean<RequestIdFilter> requestIdFilter() {
        final FilterRegistrationBean<RequestIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestIdFilter());
        return registrationBean;
    }

    @Bean
    @Order(2)
    public FilterRegistrationBean<RequestLogFilter> requestLogFilter() {
        final FilterRegistrationBean<RequestLogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestLogFilter(logRequest, logResponse, new WebLogger()));
        registrationBean.setUrlPatterns(requestLogUrls);
        return registrationBean;
    }
}
