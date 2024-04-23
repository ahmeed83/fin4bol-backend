package com.fin4bol.fin4bolbackend.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ApplicationResponseExceptionHandler  {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationErrorResponse> handleApplicationException(ApplicationException ex) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(TimeZone.getTimeZone("Europe/Amsterdam").toZoneId());
        ApplicationErrorResponse errors = new ApplicationErrorResponse();
        errors.setTimestamp(LocalDateTime.from(zonedDateTime));
        errors.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(errors, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(toList());
        return ResponseEntity.badRequest().body(errors);
    }
}
