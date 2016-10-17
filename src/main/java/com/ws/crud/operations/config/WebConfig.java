package com.ws.crud.operations.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(Integer.MAX_VALUE - 1).addResourceHandler("/webjars/**").addResourceLocations("classpath:META-INF/resources/webjars/");
        registry.setOrder(Integer.MAX_VALUE - 1).addResourceHandler("/**")
                .addResourceLocations("classpath:/")
                .addResourceLocations("classpath:META-INF/resources/")
                .addResourceLocations("classpath:resources/")
                .addResourceLocations("classpath:static/")
                .addResourceLocations("classpath:public/");
    }
}
