package com.ws.crud.operations.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ws.crud.operations.util.HeaderUtils;
import com.ws.crud.operations.util.ServiceContext;

/**
 * Handles the big job of setting up the service environment for each request.  This currently includes:
 * <p>
 * <ul>
 *     <li>Clearing the {@link ServiceContext}</li>
 *     <li>Extracting the headers and installing the values in the {@link ServiceContext}</li>
 * </ul>
 * </p>
 * Created by jograd200 on 7/15/2016.
 */
@Component
@Order(-2000)
public class ServiceEnvironmentSetupFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        ServiceContext.clear();
        HeaderUtils.addHeadersToContext((HttpServletRequest)req);
        filterChain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub - nothing to do.
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub - also nothing to do.
    }
}
