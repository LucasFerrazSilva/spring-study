package br.com.ferraz.springstudy.infra.exception;

public class ValidationException extends RuntimeException {

    private ValidationExceptionDataDTO dto;

    public ValidationException(ValidationExceptionDataDTO dto) {
        this.dto = dto;
    }

    public ValidationException(String field, String message) {
        this.dto = new ValidationExceptionDataDTO(field, message);
    }

    public ValidationExceptionDataDTO getDto() {
        return this.dto;
    }

}
