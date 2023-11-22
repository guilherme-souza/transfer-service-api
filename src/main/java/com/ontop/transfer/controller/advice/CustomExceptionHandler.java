package com.ontop.transfer.controller.advice;

import com.ontop.transfer.exception.BusinessLogicException;
import com.ontop.transfer.exception.InternalErrorException;
import com.ontop.transfer.exception.NotProcessedPaymentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(status, errors);

        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }

    @ExceptionHandler(value = { BusinessLogicException.class, NotProcessedPaymentException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.CONFLICT, ex.getMessage());
        return handleExceptionInternal(ex, apiErrorMessage,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { InternalErrorException.class })
    protected ResponseEntity<Object> handleInternalError(RuntimeException ex, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return handleExceptionInternal(ex, apiErrorMessage,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
