package br.com.ferraz.springstudy.doctor;

public record DoctorReadDTO(
        Long id,
        String name,
        String email,
        String crm,
        Expertise expertise
) {
    public DoctorReadDTO(Doctor doctor) {
        this(doctor.getId(), doctor.getName(), doctor.getEmail(), doctor.getCrm(), doctor.getExpertise());
    }
}
