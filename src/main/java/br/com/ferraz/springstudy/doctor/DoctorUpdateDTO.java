package br.com.ferraz.springstudy.doctor;

import br.com.ferraz.springstudy.address.AddressDTO;
import jakarta.validation.constraints.NotNull;

public record DoctorUpdateDTO(
        @NotNull
        Long id,
        String name,
        String phoneNumber,
        AddressDTO address
) {
}
