package com.ws.crud.operations.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;

import com.ws.crud.operations.exception.resolvers.ApplicationExceptionSoapFaultResolver;
import com.ws.crud.operations.exception.resolvers.SoapExceptionResolverMapper;
import com.ws.crud.operations.exceptions.ApplicationException;
import com.ws.crud.operations.exceptions.AuthenticationServiceException;

@SpringBootApplication
@EnableWs
@ComponentScan(basePackages={"com.ws.crud.operations"})
public class SpringBootCRUDOperations {
	
	@Autowired
	@Qualifier("MessageSource")
	private ResourceBundleMessageSource messageSource;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCRUDOperations.class, args);
	}

	 /**
     * This is the base URL which will be exposed for soap proxies, seen below.
     *
     * @param applicationContext
     * @return
     */
	@Bean
    public ServletRegistrationBean dispatcherServlet(ApplicationContext applicationContext) {
        DispatcherServlet servlet = new DispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        return new ServletRegistrationBean(servlet, "/RestCrudOperations/*");
    }

	@Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/SoapCrudOperations/*");
    }
	
	/*@Bean(name = "users")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema usersSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("UsersPort");
		wsdl11Definition.setLocationUri("/SoapCrudOperations");
		wsdl11Definition.setTargetNamespace("http://crud.ws.com/operations/types");
		wsdl11Definition.setSchema(usersSchema);
		wsdl11Definition.setFaultSuffix("Fault");
		wsdl11Definition.setRequestSuffix("Request");
		wsdl11Definition.setResponseSuffix("Response");
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema countriesSchema() {
		return new SimpleXsdSchema(new ClassPathResource("xsd/Users.xsd"));
	}*/
	
	@Bean(name = "1.0")
	public SimpleWsdl11Definition definition1607() {
		SimpleWsdl11Definition def = new SimpleWsdl11Definition();
		def.setWsdl(new ClassPathResource("/wsdls/Users_1.0.wsdl"));
		return def;
	}
	
	@Bean
	public SoapExceptionResolverMapper frameworkExceptionResolver() {
		SoapExceptionResolverMapper soapExceptionResolverMapper = new SoapExceptionResolverMapper();
		soapExceptionResolverMapper.setOrder(Integer.MIN_VALUE);
		soapExceptionResolverMapper.registerResolver(ApplicationException.class, new ApplicationExceptionSoapFaultResolver(messageSource));
		soapExceptionResolverMapper.registerResolver(AuthenticationServiceException.class, new ApplicationExceptionSoapFaultResolver(messageSource));
		return soapExceptionResolverMapper;
	}    
}
