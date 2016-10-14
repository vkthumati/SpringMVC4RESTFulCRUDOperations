package com.ws.crud.operations.interceptors.security;

import static com.google.common.collect.Maps.newConcurrentMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Component
public class TokenCachingContext extends DefaultOAuth2ClientContext {
	private static final long serialVersionUID = 1L;
	private final ClientCredentialsProvider clientCredentialsProvider;
    private final Map<Credentials, OAuth2AccessToken> tokensCache = newConcurrentMap();
    //private final CoherenceSecurityCache coherenceSecurityCache;

    /*@Autowired
    public TokenCachingContext(ClientCredentialsProvider clientCredentialsProvider, CoherenceSecurityCache coherenceSecurityCache) {
        this.clientCredentialsProvider = clientCredentialsProvider;
        this.coherenceSecurityCache = coherenceSecurityCache;
    }*/
    
    @Autowired
    public TokenCachingContext(ClientCredentialsProvider clientCredentialsProvider) {
        this.clientCredentialsProvider = clientCredentialsProvider;
    }

    @Override
    public OAuth2AccessToken getAccessToken() {
        Credentials credentials = clientCredentialsProvider.getCredentials();
        /*if(tokensCache.get(credentials) == null){
            OAuth2AccessToken token = (OAuth2AccessToken) coherenceSecurityCache.getDataFromCache(credentials.hashCode());
            if (token != null) {
            tokensCache.put(credentials, token);
            }
        }*/
        return tokensCache.get(credentials);
    }

    @Override
    public void setAccessToken(OAuth2AccessToken accessToken) {
        if (accessToken == null) {
            return;
        }

        Credentials credentials = clientCredentialsProvider.getCredentials();

        if (areCacheable(credentials)) {
            tokensCache.put(credentials, accessToken);
            //coherenceSecurityCache.putDataInCache(credentials.hashCode(), accessToken);
            //super.setAccessToken(accessToken);
        }
    }

    private boolean areCacheable(Credentials credentials) {
        return credentials != null && !credentials.equals(new Credentials());
    }
}

