package com.ws.crud.operations.interceptors.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import static org.springframework.security.oauth2.common.AuthenticationScheme.form;

@Configuration
@EnableResourceServer
@EnableWebSecurity
//@PropertySource("classpath:esp-security.properties")
//@ComponentScan("com.comcast.esp.servicecore.http")
public class SecurityConfiguration extends ResourceServerConfigurerAdapter {

    //public static final String DEFAULT_PLATFORM_SCOPE = "esp";

    @Value("${esp.rest.authenticated.paths}")
    private String authenticatedRestPaths;

    @Value("${authServer.publicKey}")
    private String publicKeyPEM;

    @Value("${authServer.scope:esp}")
    private String platformScope = "esp";

    @Bean
    public ClientCredentialsProvider clientCredentialsProvider(@Value("${oauth.server.url}") String accessTokenUri) {
        ClientCredentialsProvider details = new ClientCredentialsProvider();
        details.setAccessTokenUri(accessTokenUri);
        details.setClientAuthenticationScheme(form);
        return details;
    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(ClientCredentialsProvider clientCredentialsProvider, TokenCachingContext oAuth2ClientContext) {
        return new OAuth2RestTemplate(clientCredentialsProvider, oAuth2ClientContext);
    }

    @Bean
    JwtTokenStore jwtTokenStore() throws Exception {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setVerifierKey(publicKeyPEM);
        jwtAccessTokenConverter.afterPropertiesSet();
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                requestMatchers().
                antMatchers(authenticatedRestPaths).
                and().
                authorizeRequests().
                anyRequest().
                access("#oauth2.hasScope('" + platformScope + "')");
    }
}

