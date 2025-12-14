package com.example.gestionproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductBacklogNotAssociatedWithProjectException extends RuntimeException {
    public ProductBacklogNotAssociatedWithProjectException(String msg) {
        super(msg);
    }
}
