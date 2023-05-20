package br.com.ferraz.springstudy.domain.patient;

import br.com.ferraz.springstudy.domain.address.AddressDTO;
import jakarta.validation.constraints.NotNull;

public record UpdatePatientDTO(
        @NotNull
        Long id,
        String name,
        String email,
        String phoneNumber,
        String cpf,
        AddressDTO address
) {
}
