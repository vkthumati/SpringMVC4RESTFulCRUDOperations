package com.ws.crud.operations.interceptors.security;

import static com.google.common.collect.Lists.newArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.interceptor.EndpointInterceptorAdapter;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.SoapEndpointInterceptor;

import com.ws.crud.operations.exceptions.AuthenticationServiceException;
import com.ws.crud.operations.util.ServiceContext;

@Component
public class SoapSecurityInterceptor extends EndpointInterceptorAdapter implements SoapEndpointInterceptor {

    public static final String AUTHORIZATION = "Authorization";

    private static Logger LOG = LoggerFactory.getLogger(SoapSecurityInterceptor.class);

    private SoapSecurityHelper soapSecurityHelper;
    private AuthService authService;

    @Value("${authServer.scope:esp}")
    private String platformScope = "esp";

    @Autowired
    public SoapSecurityInterceptor(SoapSecurityHelper soapSecurityHelper, AuthService authService) {
        this.soapSecurityHelper = soapSecurityHelper;
        this.authService = authService;
    }

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        Credentials credentials = soapSecurityHelper.parseCredentialsFromHeader(messageContext);
        credentials.setScope(newArrayList(platformScope));

        try {
            LOG.info("Obtaining OAuth token for client_id={}", credentials.getUsername());
            long st = System.currentTimeMillis();

            String token = authService.getToken(credentials);

            LOG.info("Successfully obtained OAuth token={} for client_id={} in={}ms", token.substring(token.length() - 10), credentials.getUsername(), (System.currentTimeMillis() - st));

            ServiceContext.put(AUTHORIZATION, "Bearer " + token);
        } catch (OAuth2AccessDeniedException e) {
            LOG.error("Failed to get token for client_id={}", credentials.getUsername(), e);
            throw new AuthenticationServiceException(e);
        }

        return true;
    }

    @Override
    public boolean understands(SoapHeaderElement header) {
        LOG.debug("Mustunderstands:true");
        return true;
    }
}
