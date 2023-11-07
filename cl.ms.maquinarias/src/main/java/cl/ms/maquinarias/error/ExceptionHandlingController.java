package cl.ms.maquinarias.error;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {
	
	
	
	

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String message = "La estructura de la peticion es invalida";
		return buildResponseEntity(HttpStatus.BAD_REQUEST,message,ex.getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = "No se encuentra presente la variable en la ruta Endpoint";
		return buildResponseEntity(HttpStatus.BAD_REQUEST, message, ex.getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors  = result.getFieldErrors();
		List<String> errors = new ArrayList<>();
		for(FieldError fieldError : fieldErrors){
			errors.add(fieldError.getDefaultMessage());
		}
		String message = "Los datos de entrada presentan problemas";
		return buildResponseEntity(HttpStatus.BAD_REQUEST, message, errors);
		
	}
	
	@ExceptionHandler({cl.ms.maquinarias.exception.ServiceException.class})
	public ResponseEntity<Object> handleSchemaException(cl.ms.maquinarias.exception.ServiceException ex,WebRequest request){
		return buildResponseEntity(HttpStatus.valueOf(Integer.parseInt(ex.getCode())),ex.getMessage(),ex.getErrors());
	}
	
	private ResponseEntity<Object> buildResponseEntity(HttpStatus status,String message,String error){
		List<String> errors = Arrays.asList(error);
		return buildResponseEntity(status, message, errors);
	}
	
	private ResponseEntity<Object> buildResponseEntity(HttpStatus status,String message,List<String> error){
		ErrorResponse schemaError = new ErrorResponse(status.value(), message, error, LocalDateTime.now());
		return new ResponseEntity<>(schemaError,status);
	}
	
	private ResponseEntity<Object> buildResponseEntity(HttpStatus status,String message,HashMap<String,String> error){
		ErrorResponse schemaError = new ErrorResponse(status.value(), message, error, LocalDateTime.now());
		return new ResponseEntity<>(schemaError,status);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Object> handler(Exception exception){
		
		if(exception.getCause() instanceof ConstraintViolationException) {
			ConstraintViolationException cons = (ConstraintViolationException) exception.getCause().getCause();
			Set<ConstraintViolation<?>> constraintViolations  =  cons.getConstraintViolations();
			String message = "Ha ocurrido un problema de validacion con los datos de entrada";
			List<String> errors = new ArrayList<>();
			for(ConstraintViolation<?> constraintViolation : constraintViolations) {
				errors.add(constraintViolation.getMessage());
			}
			return buildResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, message, errors);
		}
		List<String> errors = Arrays.asList(exception.getMessage());
		return buildResponseEntity(HttpStatus.CONFLICT, "Error inesperado", errors);
		
	}
	
	
	
	

}
