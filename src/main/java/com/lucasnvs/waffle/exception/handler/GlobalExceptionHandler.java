package com.lucasnvs.waffle.exception.handler;

import com.lucasnvs.waffle.exception.RaffleNotFoundException;
import com.lucasnvs.waffle.exception.RaffleNotOpenException;
import com.lucasnvs.waffle.exception.TicketAlreadySoldException;
import com.lucasnvs.waffle.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                "An unexpected error occurred",
                "INTERNAL_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

