package com.ws.crud.operations.config;

import io.swagger.models.auth.OAuth2Definition;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.ImplicitGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.TokenEndpointBuilder;
import springfox.documentation.builders.TokenRequestEndpointBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.ant;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    @Bean(name="SecurityConfiguration2")
    public SecurityConfiguration securityConfiguration() {
        /*
        String clientId,
         String clientSecret,
         String realm,
         String appName,
         String apiKeyValue,
         ApiKeyVehicle apiKeyVehicle,
         String apiKeyName,
         String scopeSeparator
       */
       SecurityConfiguration securityConfiguration = 
           new SecurityConfiguration("bmsluser", "test", "realm", "appName", 
                   "apiKeyValue",  ApiKeyVehicle.HEADER, "apiKeyName", ",");
       return securityConfiguration;
    }
    
    
    
    @Bean
    public Docket restfulApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("RestCrudOperations")
                .select()
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(oauth()))
                .apiInfo(apiInfo());
    }
    
    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    
    @Bean
    SecurityContext securityContext() {
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = authScope();
        SecurityReference securityReference = SecurityReference.builder()
                .reference("user_auth")
                .scopes(scopes)
                .build();

        return SecurityContext.builder()
                .securityReferences(newArrayList(securityReference))
                .forPaths(ant("/api/.*"))
                .build();
    }

    @Bean
    SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("user_auth")
                .grantTypes(grantTypes())
                .scopes(Arrays.asList(authScope()))
                .build();
    }
    
    public AuthorizationScope authScope() {
        return new AuthorizationScope("user", "Read and write activities for User CRUD applications");
    }
    

    List<GrantType> grantTypes() {
        GrantType grantType = new ImplicitGrantBuilder()
                .loginEndpoint(new LoginEndpoint("http://petstore.swagger.io/api/oauth/dialog"))
                .build();
        
        
        TokenRequestEndpoint tre = new TokenRequestEndpointBuilder()
        .clientIdName("clientIdName")
        .clientSecretName("clientSecretName")
        .url("https://websec-dev.cable.comcast.com/as/token.oauth2")
        .build();
        
        /*
        TokenEndpoint te = new TokenEndpointBuilder()
        .tokenName("esp")
        .url("https://websec-dev.cable.comcast.com/as/token.oauth2")
        .build();
        */
        
        GrantType grantType2 = new AuthorizationCodeGrantBuilder()
        //.tokenEndpoint(te)
        .tokenRequestEndpoint(tre)
        .build();
        
        
        GrantType grantType3 = new ClientCredentialsGrant("https://websec-dev.cable.comcast.com/as/token.oauth2");
        
        
        /*
        GrantType grantType2 = new TokenRequestEndpointBuilder()
        .clientIdName("clientIdName")
        .clientSecretName("clientSecretName")
        .url("https://websec-dev.cable.comcast.com/as/token.oauth2")
        .build();
*/

        
        
       
        return newArrayList(grantType3);
    }

    private ApiInfo apiInfo() {
    	ApiInfo apiInfo = new ApiInfo(
                "User CRUD Operations",
                "CRUD Operations on User Service.",
                "1.0",
                "User CRUD API terms of service",
                new Contact("User CRUD Operations Support", 
                        "http://localhost:8080/RestCrudOperations/api/application.wadl", 
                        "info2vasanth@gmail.com"),
                "LicenseName",
                "http://localhost:8080/RestCrudOperations/api/application.wadl"
        );
    	
    	
        return apiInfo;
    }
    
    /*
    private OAuth2Definition oAuth2Definition() {
        OAuth2Definition oauth = new OAuth2Definition();
        oauth.accessCode("https://websec-dev.cable.comcast.com/as/authorization.oauth2", "https://websec-dev.cable.comcast.com/as/token.oauth2");
        oauth.scope("esp", "Read and write activities for ESP applications");
        oauth.setDescription("Requires Oauth2 security");
        oauth.setFlow("application");
        return oauth;
    }
    */
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
    }

    // ========= Beans ===========

    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver() {
        return new CookieLocaleResolver();
    }

    @Bean(name = "messageSource")
    public MessageSource getMessageSources() {
        ReloadableResourceBundleMessageSource messageSource = new     ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("/WEB-INF/resources/properties/error", "/WEB-INF/resources/properties/validation");
        messageSource.setCacheSeconds(0);

        return messageSource;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}