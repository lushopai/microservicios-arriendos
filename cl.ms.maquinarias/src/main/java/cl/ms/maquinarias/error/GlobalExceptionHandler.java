package cl.ms.maquinarias.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        // Puedes personalizar la lógica de manejo de errores aquí, como devolver un mensaje de error JSON.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de validación: " + result.getAllErrors());
    }

}
