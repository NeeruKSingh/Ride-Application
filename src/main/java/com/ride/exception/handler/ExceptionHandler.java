package com.ride.exception.handler;

import com.ride.exception.InternalServerError;
import com.ride.exception.NotFoundException;
import com.ride.exception.TokenMissingException;
import com.ride.model.response.ErrrorBaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(InternalServerError.class)
    protected ResponseEntity<Object> handleInternalServerException(InternalServerError ex, WebRequest webRequest) {
        ErrrorBaseResponse inclusiveException = ErrrorBaseResponse.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .build();
        return new ResponseEntity(inclusiveException, new HttpHeaders(), HttpStatus.OK);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest webRequest) {
        ErrrorBaseResponse inclusiveException = ErrrorBaseResponse.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .build();
        return new ResponseEntity(inclusiveException, new HttpHeaders(), HttpStatus.OK);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(TokenMissingException.class)
    protected ResponseEntity<Object> handleServiceException(TokenMissingException ex, WebRequest webRequest) {
        ErrrorBaseResponse inclusiveException = ErrrorBaseResponse.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .build();
        return new ResponseEntity(inclusiveException, new HttpHeaders(), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String message="";
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
            message= error.getField() + ": " + error.getDefaultMessage();
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
            message=error.getObjectName() + ": " + error.getDefaultMessage();
        }

        ErrrorBaseResponse apiError = ErrrorBaseResponse.builder()
                .errorCode(HttpStatus.BAD_REQUEST.name())
                .errorMessage(message).build();
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.OK, request);
    }
}
