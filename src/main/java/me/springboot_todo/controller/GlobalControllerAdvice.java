package me.springboot_todo.controller;

import lombok.extern.log4j.Log4j2;
import me.springboot_todo.exception.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Log4j2
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception e,
                                                        WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setMessage(e.getMessage());
        errorDetails.setDetails(request.getDescription(false));

        log.error(errorDetails);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
}
