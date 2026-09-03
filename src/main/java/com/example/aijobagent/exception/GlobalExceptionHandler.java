package com.example.aijobagent.exception;

import com.example.aijobagent.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException exception,
                                                                HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException exception,
                                                              HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exception,
                                                            HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, "Access denied: insufficient privileges", request, Map.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception,
                                                               HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSize(MaxUploadSizeExceededException exception,
                                                             HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "File upload size limit exceeded (maximum 5MB allowed).", request, Map.of());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(IllegalStateException exception,
                                                                   HttpServletRequest request) {
        return buildError(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(AiAgentException.class)
    public ResponseEntity<ErrorResponse> handleAiAgentException(AiAgentException exception,
                                                                HttpServletRequest request) {
        log.error("AI agent request failed for path {}", request.getRequestURI(), exception);
        return buildError(HttpStatus.BAD_GATEWAY, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception,
                                                          HttpServletRequest request) {
        Map<String, String> validationErrors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", request, validationErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        log.error("Unexpected request failure for path {}", request.getRequestURI(), exception);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", request, Map.of());
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status,
                                                     String message,
                                                     HttpServletRequest request,
                                                     Map<String, String> validationErrors) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                validationErrors
        );
        return ResponseEntity.status(status).body(response);
    }
}
