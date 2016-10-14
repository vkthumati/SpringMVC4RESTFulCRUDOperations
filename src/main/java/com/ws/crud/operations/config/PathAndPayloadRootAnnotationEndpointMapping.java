package com.ws.crud.operations.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import com.ws.crud.operations.interceptors.headers.SoapHeaderInterceptor;
import com.ws.crud.operations.interceptors.security.SoapSecurityInterceptor;

/**
 * Include this class through {@link org.springframework.context.annotation.ComponentScan} for using the version specific SOAP operation.
 * <p>Example (from version specific WSDL file)
 * <pre>
  {@code<WL5G3N1:service name="ContactService">
         <WL5G3N1:port binding="WL5G3N2:ContactServiceSoapBinding" name="ContactServicePort">
             <WL5G3N4:address location="https://cet-esp.cable.comcast.com:443/ContactService/14.11"/>
          </WL5G3N1:port>
  </WL5G3N1:service>}
 * </pre>
   This should allow for you to define a SOAP endpoint like this:
 <pre>
 {@code@PayloadRoot(namespace = NAMESPACE_URI, localPart = "/14.11/isRepeatCaller")
 @ResponsePayload
 public com.comcast.xml.contact.services.v1411.IsRepeatCallerResponse isRepeatCaller(@RequestPayload com.comcast.xml.contact.services.v1411.IsRepeatCaller isRepeatCaller, MessageContext context) {
 ...
 }}
 </pre>
 * </p>
 */
@Component
public class PathAndPayloadRootAnnotationEndpointMapping extends PayloadRootAnnotationMethodEndpointMapping {

    private static final Logger LOG = LoggerFactory.getLogger(PathAndPayloadRootAnnotationEndpointMapping.class);
    
    @Autowired
    public PathAndPayloadRootAnnotationEndpointMapping(SoapSecurityInterceptor soapSecurityInterceptor, SoapHeaderInterceptor soapHeaderInterceptor) {
    	List<EndpointInterceptor> interceptorList = new ArrayList<EndpointInterceptor>();
    	interceptorList.add(soapSecurityInterceptor);
    	interceptorList.add(soapHeaderInterceptor);
    	EndpointInterceptor[] endpointInterceptors = interceptorList.toArray(new EndpointInterceptor[interceptorList.size()]);
    	setInterceptors(endpointInterceptors);
	}

    @Override
    protected QName getLookupKeyForMessage(MessageContext messageContext) throws Exception {
        String versionFromUrl = "";
        QName payloadRootPart = this.callSuperGetLookupKeyForMessage(messageContext);
        TransportContext transportContext = TransportContextHolder.getTransportContext();
        if (transportContext != null) {
            WebServiceConnection connection = transportContext.getConnection();
            if (connection != null && connection instanceof HttpServletConnection) {
                HttpServletConnection servletConnection = (HttpServletConnection) connection;
                versionFromUrl = servletConnection.getHttpServletRequest().getRequestURI().substring(servletConnection.getHttpServletRequest().getContextPath().length());
                versionFromUrl = versionFromUrl.substring(versionFromUrl.lastIndexOf("/"));
            }
        }
        String localPart = payloadRootPart.getLocalPart();
        localPart = (localPart.startsWith("/") ? localPart : ("/"+localPart));
        QName qName = new QName(payloadRootPart.getNamespaceURI(), versionFromUrl + localPart);

        if(lookupEndpoint(qName)==null) {
            qName = new QName(payloadRootPart.getNamespaceURI(),localPart);
        }
        LOG.debug("QName: {}", qName);
        return qName;
    }

    QName callSuperGetLookupKeyForMessage(MessageContext messageContext) throws Exception {
        return super.getLookupKeyForMessage(messageContext);
    }

}

