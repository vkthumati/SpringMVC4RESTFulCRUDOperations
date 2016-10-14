package com.ws.crud.operations.exception.resolvers;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.ImmutableBiMap.of;
import static com.google.common.collect.ImmutableSortedMap.copyOf;
import static com.ws.crud.operations.exception.resolvers.SoapFaultHelper.addServiceFaultMessage;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.AbstractSoapFaultDefinitionExceptionResolver;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;

import com.ws.crud.operations.exceptions.ApplicationException;
import com.ws.crud.operations.exceptions.AuthenticationServiceException;
import com.ws.crud.operations.util.AllowedHeaders;
import com.ws.crud.operations.util.ServiceContext;

public class ApplicationExceptionSoapFaultResolver extends AbstractSoapFaultDefinitionExceptionResolver {
	
	private ResourceBundleMessageSource messageSource;

	public ApplicationExceptionSoapFaultResolver(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	public SoapFaultDefinition getFaultDefinition(Object endpoint, Exception ex) {
		
		if (ex instanceof AuthenticationServiceException) {
			SoapFaultDefinition soapFaultDefinition = new SoapFaultDefinition();
			soapFaultDefinition.setFaultCode(FaultCode.CLIENT.value());

			soapFaultDefinition.setFaultStringOrReason(ex.getMessage());
			return soapFaultDefinition;
		}		 

		Map<String, String> requestHeaders = getPrintableHeaders();

		SoapFaultDefinition soapFaultDefinition = new SoapFaultDefinition();
		soapFaultDefinition.setFaultCode(FaultCode.CLIENT.value());

		soapFaultDefinition.setFaultStringOrReason("An error occurred. Please check the detail section. [ASPECTS: ("
				+ on(",").withKeyValueSeparator("=").join(requestHeaders) + ")]");
		return soapFaultDefinition;
	}

	private Map<String, String> getPrintableHeaders() {
		Map<String, String> allowedHeaders = Stream.of(AllowedHeaders.values())
				.filter(e -> ServiceContext.containsValueForKey(e.headerName()))
				.filter(e -> !AllowedHeaders.AUTHORIZATION.headerName().equalsIgnoreCase(e.headerName()))
				.collect(toMap(e -> e.headerName(), e -> ServiceContext.getFirstString(e.headerName())));

		return copyOf(allowedHeaders);
	}
	
	@Override
	public void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
		if (ex instanceof ApplicationException) {
			ApplicationException error = (ApplicationException) ex;
			if (error.getErrorMessage() != null) {
				String message = error.getErrorMessage();
				addServiceFaultMessage(fault, of(error.getErrorCode(), message));
			}
		} else if (ex instanceof RuntimeException) {
			System.out.println("Exception Messgae : " + ex.getMessage());
			System.out.println("Exception Occured : " + ex);
			addServiceFaultMessage(fault, of(ex.getMessage(), ex.getMessage()));
		}
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
