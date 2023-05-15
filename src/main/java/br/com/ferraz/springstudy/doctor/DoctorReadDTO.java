package br.com.ferraz.springstudy.doctor;

public record DoctorReadDTO(
        String name,
        String email,
        String crm,
        Expertise expertise
) {
    public DoctorReadDTO(Doctor doctor) {
        this(doctor.getName(), doctor.getEmail(), doctor.getCrm(), doctor.getExpertise());
    }
}
