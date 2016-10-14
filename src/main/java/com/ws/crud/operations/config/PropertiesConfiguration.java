package com.ws.crud.operations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

@Configuration
public class PropertiesConfiguration {

	@Autowired
    private Environment env;

    @Bean(name = "MessageSource")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource obj = new ResourceBundleMessageSource();
        obj.setBasenames("bundles/Messages");
        return obj;
    }
}