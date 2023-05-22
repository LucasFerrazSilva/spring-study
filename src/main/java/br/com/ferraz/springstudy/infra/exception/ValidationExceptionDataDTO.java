package br.com.ferraz.springstudy.infra.exception;

import org.springframework.validation.FieldError;

public record ValidationExceptionDataDTO(String field, String message){
    public ValidationExceptionDataDTO(FieldError error){
        this(error.getField(), error.getDefaultMessage());
    }
}