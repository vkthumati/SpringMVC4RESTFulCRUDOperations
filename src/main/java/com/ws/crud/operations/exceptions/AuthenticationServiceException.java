package com.ws.crud.operations.exceptions;

import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;

public class AuthenticationServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private OAuth2AccessDeniedException e;

    public AuthenticationServiceException(OAuth2AccessDeniedException e) {
        super(e);
        this.e = e;
    }

    @Override
    public String getMessage() {
        String causeMessage = e.getCause() != null ? e.getCause().getMessage() : "unknown error";
        return e.getMessage() + " : " + causeMessage;
    }
}
