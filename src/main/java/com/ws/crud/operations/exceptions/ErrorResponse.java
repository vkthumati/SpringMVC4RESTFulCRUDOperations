package com.ws.crud.operations.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"id",
	"timestamp",
    "errorMessage",
    "errorCode",
    "type",
    "validationMessages",
    "detailMap"
})
/**
 * Represents an error message that will be sent back to a client formatted in either XML or JSON.
 * Please note:
 * The HttpStatus member is not output in the final message structure.  It is used to set the HTTP status code in the response to the client.
 */
@XmlRootElement(name = "errorResponse")
@XmlSeeAlso({ArrayList.class,HashMap.class,HashSet.class})
public class ErrorResponse {
	@XmlElement(name = "errorMessage", required = true)
	private String errorMessage;
	
	@XmlElement(name = "validationMessages", required = false)
	private List<String> validationMessages = new ArrayList<>();
	
	@XmlElement(name = "errorCode", required = true)
	private String errorCode;
	
	@XmlElement(name = "timestamp", required = true)
	private long timestamp = (new Date()).getTime();
	
	@XmlElement(name = "type")
	private String type;
	
	@XmlElement(name = "id")
	private String id;
	
	@XmlElement(name="detailMap")
	private Map<String,Object> detailMap;
	
	@XmlTransient
    private HttpStatus status;
	
	@XmlTransient
    private HttpHeaders headers = new HttpHeaders();
	
	@XmlTransient
    private Throwable cause;
	
	private List<String> errors;


	public ErrorResponse(List<String> errors) {
		this();
		this.errors = errors;
	}

	public ErrorResponse(String... errors) {
		this(Arrays.asList(errors));
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public ErrorResponse() { 
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ErrorResponse(HttpStatus status) {
        this.status = status;
    }

    public ErrorResponse(String message) {
        this(Collections.singletonList(message));
        this.setErrorMessage(message);
    }

    public ErrorResponse(String message, HttpStatus status) {
        this.setErrorMessage(message);
        this.setStatus(status);
    }
	
    public ErrorResponse(Throwable thrown) {
        this();
		this.setErrorMessage(thrown.getMessage());
		this.setType(thrown.getClass().getName());
        //this.parseException(thrown);
        this.cause = thrown;
	}

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setDetails(Map<String,Object> details) {
		this.detailMap = details;
	}
	public Map<String,Object> getDetails() {
		return this.detailMap;
	}

    /**
     * @param status The status code to use in the HTTP response to the client. This will not be included in the body. 
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    /**
     * @return
     */
    public HttpStatus getStatus() {
        return this.status;
    }
    
    public void addValidationMessage(String message) {
        if(StringUtils.isNotBlank(message)) {
            this.validationMessages.add(message);
        }
    }

    public void addValidationMessages(String message, String...messages) {
        this.addValidationMessage(message);
        if(messages!=null) {
            for(String msg : messages) {
                this.addValidationMessage(msg);
            }
        }
    }

    public void addValidationMessages(List<String> msgList) {
        if(msgList!=null) {
            for(String msg : msgList) {
                this.addValidationMessage(msg);
            }
        }
    }

    /**
     * 
     * @param messages
     */
    public void setValidationMessages(String message, String...messages) {
        List<String> newList = new ArrayList<>();
        newList.add(message);
        if(messages!=null && messages.length>0) {
            Collections.addAll(newList, messages);
        }
        this.validationMessages = newList;
    }

    /**
     * 
     * @param messageList
     */
    public void setValidationMessages(List<String> messageList) {
        if(messageList!=null) {
            this.validationMessages = new ArrayList<>(messageList);
        }
    }

    /**
     * @return Headers that should be included in the HTTP response.
     */
    @JsonIgnore
    public HttpHeaders getHeaders() {
        return this.headers;
    }

    /**
     * Throws out current headers and replaces them with the argumented headers.
     * @param headers Headers that will be added to the HTTP response. These will not be included in the response body.
     */
    @JsonIgnore
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    /**
     * Adds the argumented headers to the current headers.
     * @param headers Headers that will be added to the HTTP response. These will not be included in the response body.
     */
    @JsonIgnore
    public void addHeaders(HttpHeaders headers) {
        if(headers!=null) {
            this.headers.putAll(headers);
        }
    }

    @Override
    public String toString() {
        String ls = System.getProperty("line.separator");
        StringBuilder str = new StringBuilder();
        str.append("message="+this.getErrorMessage()+ls);
        if(StringUtils.isNotBlank(this.errorCode)) {
            str.append(ls+"code="+this.errorCode);
        }
        if(StringUtils.isNotBlank(this.type)) {
            str.append(ls+"type="+this.type);
        }
        if(StringUtils.isNotBlank(this.id)) {
            str.append(ls+"id="+this.id);
        }
        if(this.cause!=null) {
            str.append(ls+"stacktrace="+ ExceptionUtils.getStackTrace(this.cause));
        }
        if(this.validationMessages!=null && !this.validationMessages.isEmpty()) {
            str.append(ls+"validationMessages=["+String.join(ls+"    ",this.validationMessages)+ls+"]");
        }
        if(this.detailMap!=null && !this.detailMap.isEmpty()) {
            str.append(ls+"detailMap="+this.detailMap.toString());
        }
        return str.toString();
    }
}
