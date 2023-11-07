package cl.ms.maquinarias.exception;

import java.time.LocalDate;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDate timestamp;
	
	private HashMap<String, String> errors;
	
	

	public ServiceException(String code, String message) {
		super(message);
		this.code = code;
		this.timestamp = LocalDate.now();
	}
	
	

	public ServiceException(String code,String message, HashMap<String, String> errors) {
		super(message);
		this.code = code;
		this.timestamp = LocalDate.now();
		this.errors = errors;
	}
	
	



	public ServiceException(String code, String message,Throwable t) {
		super(message,t);
		this.code = code;
		this.timestamp = LocalDate.now();
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDate getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDate timestamp) {
		this.timestamp = timestamp;
	}

	public HashMap<String, String> getErrors() {
		return errors;
	}

	public void setErrors(HashMap<String, String> errors) {
		this.errors = errors;
	}
	
	

}
