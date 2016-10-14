package com.ws.crud.operations.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ServiceContext {

    public static final Logger LOG = LoggerFactory.getLogger(ServiceContext.class);

    /**
     * Add a context key here to ensure it cannot be deleted or overwritten.
     */
    public static final List<String> SYSTEM_MANAGED_KEYS = Collections.unmodifiableList(new ArrayList<String>() {{
        
    }});

    private static ThreadLocal<Map<String,Object>> contextData = new ThreadLocal<Map<String,Object>>() {
        protected synchronized Map<String,Object> initialValue() {
            Map<String,Object> threadMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            threadMap.put(ServiceConstants.GLOBAL_HEADERS.toLowerCase(), new ArrayList<String>());
            return threadMap;
        }
    };
    
    private static Object getData(String key) {
        return contextData.get().get(key.toLowerCase());
    }

    public static Map<String,Object> getReadOnlyRawData(){
        return Collections.unmodifiableMap(contextData.get());
    }

    /**
     * Clears all the stored objects.  If the object is annotated with {@link ServiceContextObject}, the provided advice will be followed.  If not, the object will be removed.
     */
    public static void clear() {
        contextData.get().entrySet().removeIf(entry-> {
            Object value = entry.getValue();
            try {
                if(value != null && value.getClass().isAnnotationPresent(ServiceContextObject.class)) {
                    ServiceContextObject scoa = value.getClass().getAnnotation(ServiceContextObject.class);
                    if(StringUtils.isNotBlank(scoa.clearMethod())) {
                        value.getClass().getMethod(scoa.clearMethod()).invoke(value);
                    } else {
                        return scoa.isTemporary();
                    }
                }

            } catch(Exception ex) {
                LOG.error("Problem while clearing ServiceContext object with key {}.  Object will be removed for safety.",entry.getKey(),ex);
            }
            return true;
        });
        contextData.get().put(ServiceConstants.GLOBAL_HEADERS, new ArrayList<>());
    }

    public static String getString(String key) {
        return (String) getData(key);
    }

    public static Integer getInteger(String key) {
        return get(key, Integer.class);
    }

    public static Long getLong(String key) {
        return (Long) getData(key);
    }

    public static Float getFloat(String key) {
        return (Float) getData(key);
    }

    public static Boolean getBoolean(String key) {
        if(containsKey(key)) {
            return (Boolean) getData(key);
        } else {
            return false;
        }
    }

    private static boolean containsKey(String key) {
        return contextData.get().containsKey(key.toLowerCase());
    }

    public static Boolean getTruthy(String key) {
        return ToolBox.isTruthy(getString(key));
    }

    /**
     * @param key The key of the thread value to retrieve.
     * @param valueClass The class of the value to be retrieved.
     * @return The retrieved value.
     */
    public static <T> T get(String key,Class<T> valueClass) {
        return (T) getData(key);
    }

    /**
     * 
     * @param key
     * @param valueClass
     * @param <T>
     * @return 
     *  - If the stored value is a list, will return the first value of that list
     *  - Otherwise will return the stored value 
     */
    public static <T> T getFirst(String key, Class<T> valueClass) {
        Object contextValue = getData(key);
        if(contextValue instanceof List) {
            return (T)((List<T>)contextValue).get(0);
        } else {
            return (T)contextValue;
        }
    }

    /**
     *
     * @param key
     * @return
     *  - If the stored value is a list, will return the first value of that list
     *  - Otherwise will return the stored value 
     */
    public static String getFirstString(String key) {
        return getFirst(key, String.class);
    }

    /**
     *
     * @param key
     * @return
     *  - If the stored value is a list, will return the first value of that list
     *  - Otherwise will return the stored value 
     */
    public static Integer getFirstInteger(String key) {
        return getFirst(key, Integer.class);
    }

    /**
     * @param key The key to store the thread value under.
     * @param value The thread value to store.
     */
    public static void put(String key, Object value) {
        key= key.toLowerCase();
        if(isSystemKey(key)) {
            LOG.warn("Cannot replace system-managed thread value with key {}",key);
        } else {
            contextData.get().put(key, value);
        }
    }

    /**
     * @param key The key of the value to remove.
     * @param valueClass The class of the value to be removed.
     * @return The removed value.
     */
    public static <T> T remove(String key,Class<T> valueClass) {
        key = key.toLowerCase();
        if(isSystemKey(key)) {
            LOG.warn("Cannot remove system-managed thread value with key {}",key);
            return null;
        } else {
            return (T)contextData.get().remove(key);
        }
    }

    public static boolean containsValueForKey(String key) {
        key = key.toLowerCase();
        return (containsKey(key) && getData(key) !=null);
    }

    /**
     * @param key The string key to check.
     * @return Returns <code>true</code> if key is for values managed by the system.
     */
    public static boolean isSystemKey(String key) {
        for(String sysKey : SYSTEM_MANAGED_KEYS) {
            if(sysKey.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

}

