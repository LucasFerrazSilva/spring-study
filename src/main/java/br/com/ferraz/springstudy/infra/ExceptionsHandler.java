package br.com.ferraz.springstudy.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleError400(MethodArgumentNotValidException exception) {
        List<FieldError> errors = exception.getFieldErrors();

        List<ValidationExceptionDataDTO> errorsDTO = errors.stream().map(ValidationExceptionDataDTO::new).toList();

        return ResponseEntity.badRequest().body(errorsDTO);
    }

    private record ValidationExceptionDataDTO(String field, String message){
        public ValidationExceptionDataDTO(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
