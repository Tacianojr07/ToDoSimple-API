package com.tacianojr07.todosimple.Exceptions;






import javax.validation.ConstraintViolationException;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tacianojr07.todosimple.services.exceptions.DataBindingViolantionException;
import com.tacianojr07.todosimple.services.exceptions.ObjectNotFoundException;

import lombok.extern.slf4j.Slf4j;
@Slf4j(topic= "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler{
    
    @Value("${server.error.include-exception}")
    private boolean printStackTrace;

    //modificando o padrão do spring sobre erros
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.");
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    //captura  erro de forma geral caso o erro que dê não esteja tratado
    public ResponseEntity<Object> handleAllUncaughtException(
        Exception exception,
        WebRequest request
    ) {
        final String errorMensage = "Unknown error occured";
        log.error(errorMensage, exception);
        return buildErrorResponse(
            exception,
            errorMensage,
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        );
    }

    private ResponseEntity<Object> buildErrorResponse(
        Exception exception,
        String message,
        HttpStatus httpStatus,
        WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (this.printStackTrace) {
            errorResponse.setStackTracer(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    //captura erro que vier do usuario ex: tentar realizar um post com um username que já existe no banco de dados
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolantionException(
        DataIntegrityViolationException dataIntegrityViolationException,
        WebRequest request
    ) {
        String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
        log.error("Failed to save entity with integrity problems: " + errorMessage, dataIntegrityViolationException);

        return buildErrorResponse(
            dataIntegrityViolationException,
            errorMessage,
            HttpStatus.CONFLICT,
            request
        );
    }

    //impede um senha seja enviado de forma vazia
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolantionException(
        ConstraintViolationException constraintViolationException,
        WebRequest request
    ) {
        log.error("Falied to validate element", constraintViolationException);
        return buildErrorResponse(
            constraintViolationException,
            HttpStatus.UNPROCESSABLE_ENTITY,
            request
        );
    }

    //erro para quando se é buscado algo na api e não for encontrado
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
        ObjectNotFoundException objectNotFoundException,
        WebRequest request
    ) {
        log.error("Falied to find the requested elemet", objectNotFoundException);
        return buildErrorResponse(
            objectNotFoundException,
            HttpStatus.NOT_FOUND,
            request
        );
    }


    @ExceptionHandler(DataBindingViolantionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolationException(
        DataBindingViolantionException dataBindingViolantionException,
        WebRequest request
    ) {
        log.error("Falied to delete entity with associated data", dataBindingViolantionException);
        return buildErrorResponse(dataBindingViolantionException, HttpStatus.CONFLICT, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
        Exception exception,
        HttpStatus httpStatus,
        WebRequest request) {
            return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
        }
    
    
}
