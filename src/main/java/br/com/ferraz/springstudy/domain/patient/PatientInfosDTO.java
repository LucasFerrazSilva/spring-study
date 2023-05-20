package br.com.ferraz.springstudy.domain.patient;

import br.com.ferraz.springstudy.domain.address.Address;

public record PatientInfosDTO(
        Long id,
        String name,
        String email,
        String cpf,
        String phoneNumber,
        Address address
) {
    public PatientInfosDTO(Patient patient) {
        this(patient.getId(), patient.getName(), patient.getEmail(), patient.getCpf(), patient.getPhoneNumber(),
                patient.getAddress());
    }
}
