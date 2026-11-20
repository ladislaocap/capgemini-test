package com.capgemini.test.code.infrastructure.web;

import com.capgemini.test.code.application.exception.DniRejectedException;
import com.capgemini.test.code.application.exception.ExternalServiceException;
import com.capgemini.test.code.application.exception.UserAlreadyExistsException;
import com.capgemini.test.code.application.exception.UserNotFoundException;
import com.capgemini.test.code.domain.user.InvalidUserDataException;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler({
      UserAlreadyExistsException.class,
      DniRejectedException.class,
      InvalidUserDataException.class
  })
  ResponseEntity<ApiError> conflict(RuntimeException exception) {
    return conflict(exception.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<ApiError> duplicateEmail(DataIntegrityViolationException exception) {
    return conflict("error validation email");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ApiError> invalidRequest(MethodArgumentNotValidException exception) {
    String field = exception.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(error -> switch (error.getField()) {
          case "email" -> "email";
          case "dni" -> "dni";
          case "rol" -> "rol";
          case "phone" -> "phone";
          default -> "userName";
        })
        .orElse("userName");
    return conflict("error validation " + field);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<ApiError> invalidParameter(ConstraintViolationException exception) {
    return conflict("error validation userName");
  }

  @ExceptionHandler(UserNotFoundException.class)
  ResponseEntity<ApiError> notFound(UserNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiError(404, exception.getMessage()));
  }

  @ExceptionHandler(ExternalServiceException.class)
  ResponseEntity<ApiError> externalServiceUnavailable(ExternalServiceException exception) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(new ApiError(503, "external service unavailable"));
  }

  private ResponseEntity<ApiError> conflict(String message) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(409, message));
  }

  public record ApiError(int code, String message) {
  }
}
