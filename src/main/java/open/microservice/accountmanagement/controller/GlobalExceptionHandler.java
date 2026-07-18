package open.microservice.accountmanagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.model.exception.ComposeFailedException;
import open.microservice.accountmanagement.model.exception.ResourceNotFoundException;
import open.microservice.accountmanagement.model.exception.ValidateFailedException;
import open.microservice.accountmanagement.model.response.interal.ErrorResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static open.microservice.accountmanagement.constant.ErrorConstant.UNPEXPECTED_ERROR;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidateFailedException.class)
    public ResponseEntity<ErrorResponseException> handleValidateFailedException(
            ValidateFailedException ex, HttpServletRequest request) {

        ErrorResponseException response = new ErrorResponseException(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                ex.getErrorDetails(),
                ex.getErrorDetail(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseException> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponseException response = new ErrorResponseException(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                ex.getErrorDetails(),
                ex.getErrorDetail(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ComposeFailedException.class)
    public ResponseEntity<ErrorResponseException> handleComposeFailedException(
            ComposeFailedException ex, HttpServletRequest request) {

        ErrorResponseException response = new ErrorResponseException(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                ex.getErrorDetails(),
                ex.getErrorDetail(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseException> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error("Unexpected exception", ex);

        ErrorResponseException response = new ErrorResponseException(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                UNPEXPECTED_ERROR,
                null,
                null,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}