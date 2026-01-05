package com.lucasnvs.waffle.common.exception.handler;

import com.lucasnvs.waffle.common.exception.RaffleNotFoundException;
import com.lucasnvs.waffle.common.exception.RaffleNotOpenException;
import com.lucasnvs.waffle.common.exception.ServiceUnavailableException;
import com.lucasnvs.waffle.common.exception.TicketAlreadySoldException;
import com.lucasnvs.waffle.common.exception.InvalidTicketNumberException;
import com.lucasnvs.waffle.common.exception.dto.ErrorResponse;
import com.lucasnvs.waffle.common.exception.dto.ValidationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        logger.warn("Validation failed with {} errors", errors.size());

        ValidationErrorResponse response = new ValidationErrorResponse(
                "Validation failed",
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.error("Invalid JSON or request format: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                "Invalid JSON format or data type. " + ex.getMostSpecificCause().getMessage(),
                "INVALID_REQUEST",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RaffleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRaffleNotFound(RaffleNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "RAFFLE_NOT_FOUND",
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RaffleNotOpenException.class)
    public ResponseEntity<ErrorResponse> handleRaffleNotOpen(RaffleNotOpenException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "RAFFLE_NOT_OPEN",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketAlreadySoldException.class)
    public ResponseEntity<ErrorResponse> handleTicketAlreadySold(TicketAlreadySoldException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "TICKET_ALREADY_SOLD",
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidTicketNumberException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTicketNumber(InvalidTicketNumberException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "INVALID_TICKET_NUMBER",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(ServiceUnavailableException ex) {
        logger.warn("Service unavailable: ", ex);
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "SERVICE_UNAVAILABLE",
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: ", ex);
        ErrorResponse error = new ErrorResponse(
                "An unexpected error occurred",
                "INTERNAL_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

