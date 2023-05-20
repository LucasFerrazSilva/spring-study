package br.com.ferraz.springstudy.domain.patient;

import br.com.ferraz.springstudy.domain.address.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record NewPatientDTO (
        @NotBlank
        String name,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "(\\()\\d{2}(\\))\\d{4,5}(-)\\d{4}", message = "Formato esperado: (xx)xxxx-xxxx ou (xx)xxxxx-xxxx")
        String phoneNumber,
        @NotBlank
        @Pattern(regexp = "\\d{9}-\\d{2}", message="Formato esperado: 123456789-12")
        String cpf,
        @NotNull
        @Valid
        AddressDTO address
) {}
