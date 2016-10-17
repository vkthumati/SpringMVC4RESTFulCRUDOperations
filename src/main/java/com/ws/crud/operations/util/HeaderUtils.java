package com.ws.crud.operations.util;

import static com.ws.crud.operations.util.ServiceConstants.GLOBAL_HEADERS;
import static com.ws.crud.operations.util.ServiceConstants.GLOBAL_NAMESPACE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * A number of convenience methods for helping to deal with the {@link ServiceContext} and managing headers.
 * Created by jograd001c on 2/26/2016.
 */
public class HeaderUtils {
    
    private static Logger LOG = LoggerFactory.getLogger(HeaderUtils.class);

    /**
     * Extracts all the global headers from the {@link ServiceContext}.
     * @return A map of the headers names and their value lists.
     */
    public static Map<String, List<String>> extractGlobalHeadersFromContext() {
        List<String> hdrNames = ServiceContext.get(GLOBAL_HEADERS,List.class);
        Map<String,List<String>> hdrMap = new HashMap<>();
        for(String hdrName : hdrNames) {
            Object value = ServiceContext.get(hdrName,Object.class);
            if(value instanceof String) {
                hdrMap.put(hdrName,new ArrayList<String>() {{ add((String)value); }});
            } else if(value instanceof List) {
                hdrMap.put(hdrName,(List)value);
            }
        }
        return hdrMap;
    }

    /**
     * Extracts all the headers described by {@link AllowedHeaders} from the {@link ServiceContext}.
     * @return A map of the headers names and their value lists.
     */
    public static Map<String, List<String>> extractAllowedHeadersFromContext() {
        Map<String,List<String>> hdrMap = new HashMap<>();
        for(AllowedHeaders header : AllowedHeaders.values()) {
            String headerName = header.headerName();
            Object value = ServiceContext.get(headerName,Object.class);
            if(value instanceof String) {
                hdrMap.put(headerName,new ArrayList<String>() {{ add((String)value); }});
            } else if(value instanceof List) {
                hdrMap.put(headerName,(List)value);
            }
        }
        return hdrMap;
    }


    /**
     * Adds a header to the collection in {@link ServiceContext}.  If the header does not already exist, it is created and the value is put into a list.  If the header does exist, the value is added to the existing value list.
     * @param name The header name to add.
     * @param value The header value to add.
     */
    public static void addHeaderToContext(String name, String value) {
        if(ServiceContext.containsValueForKey(name)) {
            ServiceContext.get(name,List.class).add(sanitize(value));
        } else {
            List<String> values = new ArrayList<>();
            values.add(sanitize(value));
            ServiceContext.put(name,values);
            registerHeaderInContext(name);
        }
    }

    /**
     * Adds values to the specified header in {@link ServiceContext}
     * @param name The header name.
     * @param values The values of the header.
     */
    public static void addHeaderToContext(String name, List<String> values) {
        if(ServiceContext.containsValueForKey(name)) {
            ServiceContext.get(name,List.class).addAll(sanitize(values));
        } else {
            ServiceContext.put(name,new ArrayList<>(sanitize(values)));
            registerHeaderInContext(name);
        }
    }

    /**
     * Adds a map of headers and their values to the {@link ServiceContext}
     * @param valuesMap A map of header names and values.  The values can be either an String value which will be added to any existing values for the header, or a {@link List<String>} implementation, in which case all the values are added. 
     */
    public static void addHeadersToContext(Map<String,Object> valuesMap) {
        for(Map.Entry<String,Object> entry : valuesMap.entrySet()) {
            if(entry.getValue() instanceof List) {
                addHeaderToContext(entry.getKey(),(List)entry.getValue());
            } else if(entry.getValue() instanceof String) {
                addHeaderToContext(entry.getKey(),(String)entry.getValue());
            }
        }
    }

    /**
     * Adds all the headers contained in a {@link HttpHeaders} instance to the {@link ServiceContext}.
     * @param headers The headers instance containing the names and values to add.
     */
    public static void addHeadersToContext(HttpHeaders headers) {
        for(Map.Entry<String,List<String>> entry : headers.entrySet()) {
            String hdrName = entry.getKey();
            if (hdrName.startsWith(GLOBAL_NAMESPACE)) {
                String strippedHdrName = hdrName.substring(GLOBAL_NAMESPACE.length());
                addHeaderToContext(strippedHdrName,entry.getValue());
            } else {
                ServiceContext.put(hdrName, sanitize(entry.getValue()));
            }
        }
    }

    /**
     * Extracts headers from the {@link HttpServletRequest} object and adds them to the {@link ServiceContext}
     * @param req The servlet request to extract the headers from.
     */
    public static void addHeadersToContext(HttpServletRequest req) {
        ToolBox.forEachRemaining(req.getHeaderNames(), hdrName -> {
            Enumeration<String> values = req.getHeaders(hdrName);
            if (values != null && values.hasMoreElements()) {
                if (hdrName.startsWith(GLOBAL_NAMESPACE)) {
                    String strippedHdrName = hdrName.substring(GLOBAL_NAMESPACE.length());
                    ServiceContext.put(strippedHdrName, Collections.list(values));
                    ServiceContext.get(GLOBAL_HEADERS, List.class).add(strippedHdrName);
                } else {
                    ServiceContext.put(hdrName, Collections.list(values));
                }
            }
        });
    }

    /**
     * Registers the given name as a header in the {@link ServiceContext}.  This does not add the actual header name or a <code>null</code> value to the context, and a call to {@link ServiceContext#containsKey(String)} for the name will return <code>false</code>. 
     * @param name The name to register as a header.
     */
    public static void registerHeaderInContext(String name) {
        if(!ServiceContext.get(GLOBAL_HEADERS,List.class).contains(name)) {
            ServiceContext.get(GLOBAL_HEADERS,List.class).add(name);
        }
    }


    /**
     * Appends all headers and their values currently existing in the {@link ServiceContext} to the given HTTP request instance.
     * <p>The headers processed are the "global" headers denoted by the {@link com.comcast.esp.servicecore.constants.ServiceConstants#GLOBAL_NAMESPACE}, and the headers listed in {@link AllowedHeaders}</p>
     * @param headers The headers instance to add the context values to.
     */
    public static void appendHeaders(HttpHeaders headers) {
        extractGlobalHeadersFromContext().forEach((key,values) -> {
            for(String value : values) {
                headers.add(GLOBAL_NAMESPACE + key, sanitize(value));
            }
        });
        extractAllowedHeadersFromContext().forEach((key,values) -> {
            if(!headers.containsKey(GLOBAL_NAMESPACE + key)) {
                for(String value : values) {
                    if(AllowedHeaders.get(key).isHttpHeader()) {
                        headers.add(key, sanitize(value));
                    } else {
                        headers.add(GLOBAL_NAMESPACE + key, sanitize(value));
                    }
                }
            }
        });
    }

    /**
     * Appends all headers and their values currently existing in the {@link ServiceContext} to the given HTTP request instance.
     * @param response The {@link HttpServletResponse} instance to which all current headers will be added.
     */
    public static void appendHeaders(HttpServletResponse response) {
        extractGlobalHeadersFromContext().forEach((key,values) -> {
            for(String value : values) {
                response.addHeader(GLOBAL_NAMESPACE + key, sanitize(value));
            }
        });
        extractAllowedHeadersFromContext().forEach((key,values) -> {
            if(!response.containsHeader(GLOBAL_NAMESPACE + key)) {
                for(String value : values) {
                    if(AllowedHeaders.get(key).isHttpHeader()) {
                        response.addHeader(key, sanitize(value));
                    } else {
                        response.addHeader(GLOBAL_NAMESPACE + key, sanitize(value));
                    }
                }
            }
        });
    }

    /**
     * Combines a number of {@link HttpHeaders} instances together.  The returned instance is created new - none of the argumented headers instances are modified.
     * @param headers
     * @return A new {@HttpHeaders} instances containing the sum of header entries.
     */
    public static HttpHeaders combineHeaders(HttpHeaders...headers) {
        return combineHeaders(Arrays.asList(headers));
    }
    
    /**
     * Combines a list of {@link HttpHeaders} instances together.  The returned instance is created new - none of the argumented headers instances are modified.
     * @param hdrList A list of HttpHeader instances to combine into one HttpHeaders instance.
     * @return A new {@HttpHeaders} instances containing the sum of header entries.
     */
    public static HttpHeaders combineHeaders(List<HttpHeaders> hdrList) {
        HttpHeaders combinedHdrs = new HttpHeaders();
        List<String> hdrValues;
        for(HttpHeaders hdrs : hdrList) {
            for(Map.Entry<String,List<String>> hdrEntry : hdrs.entrySet()) {
                hdrValues = sanitize(hdrEntry.getValue());
                if(combinedHdrs.containsKey(hdrEntry.getKey())) {
                    combinedHdrs.get(hdrEntry.getKey()).addAll(hdrValues);
                } else {
                    combinedHdrs.put(hdrEntry.getKey(),new LinkedList<String>(hdrValues));
                }
            }
        }
        return combinedHdrs;
    }

    /**
     * Retrieves all the values for a given header 
     * @param hdrName The name of the header for which to retrieve the values.
     * @return The list of values for the header.
     */
    public static List<String> getValues(String hdrName) {
        if(ServiceContext.get(GLOBAL_HEADERS,List.class).contains(hdrName)) {
            List<String> values = null;
            try {
                values = ServiceContext.get(hdrName,List.class);
            } catch(Throwable thrown) {
                LOG.error("Value for header '"+hdrName+"' in ServiceContext is not a list: "+thrown.getMessage());
                return null;
            }
            return values;
        } else {
            return null;
        }
    }

    /**
     * Retrieves the first value of a header's values
     * @param hdrName The name of the header.
     * @return The first string value of the header, or <code>null</code> if the header does not exist or has no values.
     */
    public static String getStringValue(String hdrName) {
        List<String> values = getValues(hdrName);
        return ((values!=null && !values.isEmpty()) ? values.get(0) : null);
    }

    /**
     * @param hdrName The name of the header for which to check for values.
     * @return Returns <code>true</code> if there are any values for the header name.  Returns <code>false</code> if the heaader doesn't exist, has no values, or the value in the ServiceContext is not a List object.
     */
    public static boolean hasValue(String hdrName) {
        List<String> values = getValues(hdrName);
        return (values!=null && !values.isEmpty());
    }

    /**
     * @param hdrName The name of the header for which to check for values.
     * @return Returns <code>true</code> if the header exists, ignoring whether or not it has any values. Otherwise returns <code>false</code>.
     */
    public static boolean exists(String hdrName) {
        return ServiceContext.get(GLOBAL_HEADERS,List.class).contains(hdrName);
    }
    
    /**
     * Removes leading, trailing whitespace as well as any 
     * newline or return characters from a list of header
     * values.
     * 
     * @param values The list of header values.
     * @return The same list of values, trimmed and without newlines or returns.
     */
    private static List<String> sanitize(List<String> values) {
        if(values!=null) {
            for(int i = 0; i < values.size(); i++) {
                values.set(i, sanitize(values.get(i)));
            }
        }
        return values;
    }
    
    /**
     * Removes leading, trailing whitespace as well as any 
     * newline or return characters from a string.
     * 
     * @param value The a header string.
     * @return The the header without newlines or returns.
     */
    private static String sanitize(String value) {
        return (value==null ? null : StringUtils.remove(StringUtils.remove(value.trim(), "\n"), "\r"));
    }
}
