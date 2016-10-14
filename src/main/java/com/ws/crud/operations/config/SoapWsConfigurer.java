package com.ws.crud.operations.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;

import com.ws.crud.operations.interceptors.headers.SoapHeaderInterceptor;
import com.ws.crud.operations.interceptors.security.SoapSecurityInterceptor;

@Component
public class SoapWsConfigurer extends WsConfigurerAdapter{

	@Autowired
	private SoapHeaderInterceptor soapHeaderInterceptor;
	@Autowired
	private SoapSecurityInterceptor soapSecurityInterceptor;
	
	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(this.soapHeaderInterceptor);
		interceptors.add(this.soapSecurityInterceptor);
	}
}
