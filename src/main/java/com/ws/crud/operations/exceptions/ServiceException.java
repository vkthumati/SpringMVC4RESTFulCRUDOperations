package com.ws.crud.operations.exceptions;

import ch.qos.logback.classic.Level;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the base of the exception hierarchy for the ESP REST services.  If you need an exception, use this, or subclass it.
 * <p>This class provides a detail map member for passing along exception metadata.</p>
 * Created by jograd001c on 4/21/2015.
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Map<String,Object> detailMap = new HashMap<>();
    private long timestamp = (new Date()).getTime();
    private String code;
    private Level logLevel = Level.ERROR;
    public static final String KEY_ERRORRESPONSE = "errorResponse";
    public static final String NO_ERROR_RESPONSE = "No ErrorResponse";

    /**
     *
     * @param message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param details
     */
    public ServiceException(String message, Map<String,Object> details) {
        super(message);
        this.detailMap = details;
    }
    /**
     *
     * @param message
     * @param cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param details
     */
    public ServiceException(String message, Throwable cause, Map<String,Object> details) {
        super(message, cause);
        this.detailMap = details;
    }

    /**
     *
     * @param cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param cause
     */
    public ServiceException(Throwable cause, Map<String,Object> details) {
        super(cause);
        this.detailMap = details;
    }

    public ServiceException(ErrorResponse errorResponse) {
        super("Exception: "+(errorResponse!=null?errorResponse.getErrorMessage(): NO_ERROR_RESPONSE));
        this.addDetail(KEY_ERRORRESPONSE,errorResponse);
    }

    public ErrorResponse getErrorResponse() {
        return this.getDetail(KEY_ERRORRESPONSE,ErrorResponse.class);
    }
    
    public void setDetails(Map<String,Object> details) {
        if(details!=null) {
            this.detailMap = new HashMap<>(details);
        }
    }

    /**
     * Used to add extra details of the exception.
     * @param key The key of the detail being added.
     * @param value The value of the detail being added.
     */
    public void addDetail(String key, Object value) {
        this.detailMap.put(key, value);
    }

    /**
     * Convenience method that checks if a specific key exists in the internal details map.
     * @param key The key of the detail entry to check for.
     * @return
     */
    public boolean hasDetail(String key) {
        return this.detailMap.containsKey(key);
    }

    public boolean hasDetails() {
        return this.detailMap.isEmpty();
    }

    /**
     * @return The internal detail map.  Developer beware - this is not a copy, so what you do to the returned reference affects the source ServiceException.
     */
    public Map<String,Object> getDetails() {
        return this.detailMap;
    }

    /**
     * @param key The key of the detail value to retrieve.
     * @param valueClass The class of the detail value being retrieved.
     * @param <T>
     * @return
     */
    public <T> T getDetail(String key, Class<T> valueClass) {
        return (T)this.detailMap.get(key);
    }

    /**
     * @param timestamp A timestamp for when the actual exception occurred.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @param date A timestamp for when the actual exception occurred.
     */
    public void setTimestamp(Date date) {
        if(date!=null) {
            this.timestamp = date.getTime();
        }
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * @return
     */
    public boolean hasCode() {
        return StringUtils.isNotBlank(this.code);
    }

    /**
     * Allows for suggesting the level at which the exception should be logged.  Specific code would need to be written to check this value and then follow its advice as to what level at which the exception should be logged and subsequently what the logging format should be for that level. 
     * <p>The default logging level is <code>ERROR</code></p>
     * @param logLevel The suggested level at which the exception should be logged.
     */
    public void setLogLevel(Level logLevel) {
        this.logLevel = (logLevel==null || logLevel==Level.ALL ? Level.ERROR : logLevel);
    }

    /**
     * Specific code would need to be written to check the returned value and then follow its advice as to what level at which the exception should be logged and subsequently what the logging format should be for that level. 
     * @return The suggested level at which the exception should be logged.
     */
    public Level getLogLevel() {
        return this.logLevel;
    }
}

