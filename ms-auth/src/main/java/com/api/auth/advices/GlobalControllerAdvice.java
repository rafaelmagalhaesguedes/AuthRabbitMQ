package com.api.auth.advices;

import com.api.auth.services.exception.DuplicateEmailException;
import com.api.auth.services.exception.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the API.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

  /**
   * Handle NotFoundException.
   *
   * @param exception the exception
   * @return the response entity
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFound(NotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(exception.getMessage());
  }

  /**
   * Handle MethodArgumentNotValidException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle DuplicateEmailException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
  }

  /**
   * Handle generic exceptions.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleOtherExceptions(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An unexpected error occurred: " + ex.getMessage());
  }
}
