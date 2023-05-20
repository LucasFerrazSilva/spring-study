package br.com.ferraz.springstudy.domain.patient;

public record PatientListDTO(
        Long id,
        String name,
        String email,
        String phoneNumber,
        String cpf
) {
    public PatientListDTO(Patient patient) {
        this(patient.getId(), patient.getName(), patient.getEmail(), patient.getPhoneNumber(), patient.getCpf());
    }
}
