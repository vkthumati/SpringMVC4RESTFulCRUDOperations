package com.ws.crud.operations.exceptions;

public class ApplicationException extends RuntimeException{
	
	private static final long serialVersionUID = -1852445299929954831L;
	
	private String errorCode;
	private String errorMessage;

	public ApplicationException(String errorCode, String errorMessage){
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
