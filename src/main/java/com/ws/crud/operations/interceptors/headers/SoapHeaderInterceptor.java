package com.ws.crud.operations.interceptors.headers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.interceptor.EndpointInterceptorAdapter;

import com.ws.crud.operations.util.ServiceContext;

import static com.ws.crud.operations.util.ServiceConstants.*;

@Component
public class SoapHeaderInterceptor extends EndpointInterceptorAdapter{

    private SoapHeaderHelper soapHeaderHelper;

    @Autowired
    public SoapHeaderInterceptor(SoapHeaderHelper soapHeaderHelper) {
        this.soapHeaderHelper = soapHeaderHelper;
    }

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        Map<String, String> headerMap = soapHeaderHelper.getRequestHeaderMap(messageContext);
        if(headerMap!=null) {
            headerMap.entrySet().forEach(entry -> {
                ServiceContext.get(GLOBAL_HEADERS, List.class).add(entry.getKey());
            });
            headerMap.entrySet().forEach(entry -> ServiceContext.put(entry.getKey(), entry.getValue()));
        }
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
        soapHeaderHelper.setDefaultSoapResponseHeader(messageContext);
        return true;
    }
    
    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
    	soapHeaderHelper.setDefaultSoapResponseHeader(messageContext);
        return true;
    }

    private boolean notTypeProperty(Map.Entry<String, String> e) {
        return !e.getKey().equals("class");
    }
}
