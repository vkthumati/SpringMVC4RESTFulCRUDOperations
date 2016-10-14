package com.ws.crud.operations.interceptors.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthService {

    private OAuth2RestTemplate oAuth2RestTemplate;
    private ClientCredentialsProvider clientCredentialsProvider;

    @Autowired
    public AuthService(OAuth2RestTemplate oAuth2RestTemplate, ClientCredentialsProvider clientCredentialsProvider) {
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.clientCredentialsProvider = clientCredentialsProvider;
    }

    public String getToken(Credentials credentials) {
        clientCredentialsProvider.setCredentials(credentials);
        return oAuth2RestTemplate.getAccessToken().getValue();
    }

}
