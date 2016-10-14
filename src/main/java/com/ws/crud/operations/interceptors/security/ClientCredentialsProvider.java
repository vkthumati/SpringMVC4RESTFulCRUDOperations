package com.ws.crud.operations.interceptors.security;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.ThreadLocal.withInitial;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

import java.util.List;

import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

public class ClientCredentialsProvider extends ClientCredentialsResourceDetails {

    private ThreadLocal<Credentials> credentialsStore = withInitial(Credentials::new);

    @Override
    public String getClientId() {
        return getCredentials().getUsername();
    }

    @Override
    public String getClientSecret() {
        return getCredentials().getPassword();
    }

    @Override
    public boolean isScoped() {
        return !isEmpty(getScope());
    }

    @Override
    public List<String> getScope() {
        return safeCopy(getCredentials().getScope());
    }

    @Override
    public void setScope(List<String> scope) {
        Credentials credentials = getCredentials();
        credentials.setScope(safeCopy(scope));
        credentialsStore.set(credentials);
    }

    @Override
    public boolean isAuthenticationRequired() {
        return hasText(this.getClientId()) && AuthenticationScheme.none != this.getAuthenticationScheme();
    }

    public void setCredentials(Credentials credentials) {
        credentialsStore.set(new Credentials(credentials.getUsername(), credentials.getPassword(), safeCopy(credentials.getScope())));
    }

    public Credentials getCredentials() {
        Credentials credentials = credentialsStore.get();
        return new Credentials(credentials.getUsername(), credentials.getPassword(), safeCopy(credentials.getScope()));
    }

    private List<String> safeCopy(List<String> scope) {
        return scope == null ? null : newArrayList(scope);
    }
}

