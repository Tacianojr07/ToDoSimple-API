package com.tacianojr07.todosimple.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataBindingViolantionException extends DataIntegrityViolationException {


    public DataBindingViolantionException(String message) {
        super(message);
    }
}