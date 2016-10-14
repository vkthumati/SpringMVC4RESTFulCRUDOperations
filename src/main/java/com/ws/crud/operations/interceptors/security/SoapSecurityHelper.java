package com.ws.crud.operations.interceptors.security;

import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.xpath.AbstractXPathTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;

import javax.xml.transform.Source;
import java.util.Iterator;

@Component
public class SoapSecurityHelper {

    private static final String USERNAME_XPATH = "//*[local-name()='Username']/text()";
    private static final String PASSWORD_XPATH = "//*[local-name()='Password']/text()";
    private static final String SECURITY_XPATH = "//*[local-name()='Security']";

    private AbstractXPathTemplate xPathTemplate = new Jaxp13XPathTemplate();

    public Credentials parseCredentialsFromHeader(MessageContext messageContext) {
        SoapMessage request = (SoapMessage) messageContext.getRequest();
        Iterator<SoapHeaderElement> soapHeaderElementIterator = request.getSoapHeader().examineAllHeaderElements();
        while (soapHeaderElementIterator.hasNext()) {
            Source source = soapHeaderElementIterator.next().getSource();
            if (xPathTemplate.evaluateAsBoolean(SECURITY_XPATH, source)) {
                String username = xPathTemplate.evaluateAsString(USERNAME_XPATH, source);
                String password = xPathTemplate.evaluateAsString(PASSWORD_XPATH, source);
                return new Credentials(username, password);
            }
        }
        return new Credentials();
    }
}