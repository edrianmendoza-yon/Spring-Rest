package com.spring.springrest.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.spring.springrest.entity.StudentErrorResponse;
import com.spring.springrest.exception.StudentNotFoundException;

@ControllerAdvice
public class StudentExceptionHandler {
    @ExceptionHandler public ResponseEntity<StudentErrorResponse> handleException( StudentNotFoundException exc) { 
        StudentErrorResponse
        error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis()); 
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); 
      }
     
    
    @ExceptionHandler public ResponseEntity<StudentErrorResponse> handleException(Exception exc) { 
        StudentErrorResponse error = new
        StudentErrorResponse(); error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Invalid Data. Only Integers are allowed!" +
        exc.getMessage()); error.setTimestamp(System.currentTimeMillis()); 
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); 
    }
     
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException exc) {
        /*
         * Map<String, String> errors = new HashMap<>();
         * ex.getBindingResult().getAllErrors().forEach((error) -> { String
         * fieldName = ((FieldError) error).getField(); String errorMessage =
         * error.getDefaultMessage(); errors.put(fieldName, errorMessage); });
         */
        Map<String, String> errors = new HashMap<>();
        exc.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
