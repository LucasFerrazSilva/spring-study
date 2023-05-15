package br.com.ferraz.springstudy.doctor;

import br.com.ferraz.springstudy.address.AddressDTO;

public record DoctorDTO(
        String name,
        String email,
        String crm,
        Expertise expertise,
        AddressDTO address
) {}
