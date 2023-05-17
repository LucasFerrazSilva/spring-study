package br.com.ferraz.springstudy.domain.doctor;

import br.com.ferraz.springstudy.domain.address.AddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DoctorCreateDTO(
        @NotBlank(message="O nome não pode estar vazio.")
        String name,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp="\\d{4,6}")
        String crm,
        @NotBlank
        String phoneNumber,
        @NotNull
        Expertise expertise,
        @NotNull
        @Valid
        AddressDTO address
) {}
