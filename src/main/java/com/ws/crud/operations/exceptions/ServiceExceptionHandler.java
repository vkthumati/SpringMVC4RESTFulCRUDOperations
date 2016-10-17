package com.ws.crud.operations.exceptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Configurable
@EnableWebMvc
@RestControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Throwable.class)
	@ResponseBody()
	public ResponseEntity<Object> handleControllerException(HttpServletRequest req, Throwable ex) {
		ErrorResponse errorResponse = null;
		HttpStatus status = null;

		if (ex instanceof ApplicationException) {
			ApplicationException ae = (ApplicationException) ex;
			errorResponse = new ErrorResponse();
			if (ae != null && ae.getErrorMessage() != null) {
				errorResponse.setErrorCode(ae.getErrorCode());
				errorResponse.setErrorMessage(ae.getErrorMessage());
			}
			errorResponse.setStatus(HttpStatus.BAD_REQUEST);
			errorResponse.setTimestamp(Calendar.getInstance().getTimeInMillis());
			status = errorResponse.getStatus();
	    } else {
            errorResponse = new ErrorResponse(ex);
            status = errorResponse.getStatus();
        }

		Object response = null;
		if (errorResponse != null) {
			response = errorResponse;
			System.out.println("[" + req.getMethod() + "::" + req.getContextPath() + "]   ErrorResponse: HttpStatus["
					+ errorResponse.getStatus() + "]  code[" + errorResponse.getErrorCode() + "]  message[" + errorResponse.getErrorMessage() + "]   timestamp["
					+ errorResponse.getTimestamp() + "]");
			System.out.println(req.getParameterNames() + "  xxxx  " + req.getParameterMap());
		} 
		return new ResponseEntity<Object>(response, status);
	}	 

	
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String notAccepted = "UnAccepted content type : " + ex.getSupportedMediaTypes();
        String accepted = "Accepted content types : " + MediaType.toString(headers.getAccept());
        ErrorResponse errorResponse = new ErrorResponse(notAccepted, accepted);
        return new ResponseEntity<Object>(errorResponse, headers, status);
	} 
	 
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		return super.handleMissingPathVariable(ex, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		return super.handleMissingServletRequestParameter(ex, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String unsupported = "Unsupported content type : " + ex.getContentType();
        String supported = "Supported content types : " + MediaType.toString(ex.getSupportedMediaTypes());
        ErrorResponse errorResponse = new ErrorResponse(unsupported, supported);
        return new ResponseEntity<Object>(errorResponse, headers, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
		List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
		String error;
		for (FieldError fieldError : fieldErrors) {
			error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
			errors.add(error);
		}
		for (ObjectError objectError : globalErrors) {
			error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
			errors.add(error);
		}
		ErrorResponse errorMessage = new ErrorResponse(errors);
		return new ResponseEntity<Object>(errorMessage, headers, status);
	}
	
}
