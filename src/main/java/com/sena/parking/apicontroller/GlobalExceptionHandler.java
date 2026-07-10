package com.sena.parking.apicontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sena.parking.exception.BusinessRuleException;
import com.sena.parking.exception.DuplicateResourceException;
import com.sena.parking.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<Map<String, String>> handleDuplicate(DuplicateResourceException ex) {
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<Map<String, String>> handleBusinessRule(BusinessRuleException ex) {
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		return buildResponse(HttpStatus.CONFLICT,
				"Error de integridad de datos: " + ex.getMostSpecificCause().getMessage());
	}

	// Red de seguridad final: errores no anticipados -> 500 real, no 400 falso.
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
	}

	private ResponseEntity<Map<String, String>> buildResponse(HttpStatus status, String message) {
		Map<String, String> error = new HashMap<>();
		error.put("error", message);
		return ResponseEntity.status(status).body(error);
	}

}
