package br.com.ferraz.springstudy.doctor;

import br.com.ferraz.springstudy.address.Address;

public record DoctorInfosDTO(
        Long id,
        String name,
        String email,
        String crm,
        String phoneNumber,
        Expertise expertise,
        Address address
) {
    public DoctorInfosDTO(Doctor doctor) {
        this(doctor.getId(), doctor.getName(), doctor.getEmail(), doctor.getCrm(), doctor.getPhoneNumber(),
                doctor.getExpertise(), doctor.getAddress());
    }
}
