package com.ws.crud.operations.util;

import java.lang.annotation.*;

/**
 * Provides management metadata for classes that will have instances stored in the {@link com.comcast.esp.servicecore.http.ServiceContext}.  
 * 
 * Created by jograd001c on 12/29/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ServiceContextObject {
    /**
     * The name of the method on the object to call when the service context is cleared.
     * @return The name of the clear method.
     */
    String clearMethod() default "";

    /**
     * Denotes if a context object is temporary.  If <code>true</code>, the object will be cleared from the context before each new request is serviced. The default is <code>false</code>.
     * @return <code>true</code> if the object should be removed before each new request, or <code>false</code> if the object may live through all requests.
     */
    boolean isTemporary() default false;
}

