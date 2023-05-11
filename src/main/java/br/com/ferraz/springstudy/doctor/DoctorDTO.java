package br.com.ferraz.springstudy.doctor;

import br.com.ferraz.springstudy.address.Address;

public record DoctorDTO(
        String name,
        String email,
        String crm,
        Expertise expertise,
        Address address
) {}
