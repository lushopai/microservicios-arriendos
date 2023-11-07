package cl.ms.maquinarias.error;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorResponse {
	
	private final int code;
	private final String message;
	private final List<String> errors;
	private final HashMap<String, String> errorsDetails;
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy hh:mm:ss")
	private final LocalDateTime timestamp;
	
	
	
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public List<String> getErrors() {
		return errors;
	}
	public HashMap<String, String> getErrorsDetails() {
		return errorsDetails;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public ErrorResponse(int code, String message,List<String> errors, LocalDateTime timestamp) {
		super();
		this.code = code;
		this.message = message;
		this.timestamp = timestamp;
		this.errors = errors;
		this.errorsDetails = null;
	}
	
	
	
	public ErrorResponse(int code, String message, HashMap<String, String> error,
			LocalDateTime timestamp) {
		super();
		this.code = code;
		this.message = message;
		errors = Arrays.asList("ServiceException");
		this.errorsDetails = error;
		this.timestamp = timestamp;
	}
	
	public ErrorResponse(int code, String message, String error,LocalDateTime timestamp ) {
		super();
		this.code = code;
		this.message = message;
		errors = Arrays.asList(error);
		this.timestamp = timestamp;
		this.errorsDetails = null;
	
	}
	
	
	
	
	
	
	
	
	
	

}
