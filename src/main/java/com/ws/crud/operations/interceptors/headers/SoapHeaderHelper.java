package com.ws.crud.operations.interceptors.headers;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;

import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Node;

import com.ws.crud.operations.util.ServiceContext;
import com.ws.crud.operations.types.RequestHeaderType;
import com.ws.crud.operations.types.ResponseHeaderType;

@Component
public class SoapHeaderHelper {

    private static final String REQUEST_HEADER = "requestHeader";

    public RequestHeaderType getSoapRequestHeader(MessageContext messageContext) {
        SoapMessage request = (SoapMessage) messageContext.getRequest();
        Iterator<SoapHeaderElement> soapHeaderElementIterator = request.getSoapHeader().examineAllHeaderElements();
        while (soapHeaderElementIterator.hasNext()) {
            SoapHeaderElement element = soapHeaderElementIterator.next();
            if (REQUEST_HEADER.equals(element.getName().getLocalPart())) {
                return JAXB.unmarshal(element.getSource(), RequestHeaderType.class);
            }
        }
        return null;
    }


    public Map<String, String> getRequestHeaderMap(MessageContext messageContext) {
        SoapMessage request = (SoapMessage) messageContext.getRequest();
        Iterator<SoapHeaderElement> soapHeaderElementIterator = request.getSoapHeader().examineAllHeaderElements();
        while (soapHeaderElementIterator.hasNext()) {
            SoapHeaderElement element = soapHeaderElementIterator.next();
            if (REQUEST_HEADER.equals(element.getName().getLocalPart())) {
                return this.parseChildNodes(((DOMSource) element.getSource()).getNode().getFirstChild());
            }
        }
        return null;
    }

    private Map<String, String> parseChildNodes(Node child) {
        Node nextChild = child;
        Map<String, String> headerValueMap = new HashMap<>();
        do {
            if (nextChild instanceof SOAPElement) {
                SOAPElement valueElmt = (SOAPElement) nextChild;
                headerValueMap.put(valueElmt.getLocalName(), valueElmt.getFirstChild() != null ? valueElmt.getFirstChild().getNodeValue() : "");
            }
            nextChild = nextChild.getNextSibling();
        } while (nextChild != null);
        return headerValueMap;
    }

    public void setDefaultSoapResponseHeader(MessageContext messageContext) throws DatatypeConfigurationException {
        SoapMessage response = (SoapMessage) messageContext.getResponse();
        Result result = response.getSoapHeader().getResult();

        ResponseHeaderType responseHeader = new ResponseHeaderType();
        responseHeader.setTrackingId(ServiceContext.getFirstString("trackingId"));
        responseHeader.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));

        JAXB.marshal(responseHeader, result);
    }
}
