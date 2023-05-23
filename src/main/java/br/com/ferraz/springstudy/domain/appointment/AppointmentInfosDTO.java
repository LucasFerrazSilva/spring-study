package br.com.ferraz.springstudy.domain.appointment;

import br.com.ferraz.springstudy.domain.doctor.DoctorInfosDTO;
import br.com.ferraz.springstudy.domain.patient.PatientInfosDTO;

import java.time.LocalDateTime;

public record AppointmentInfosDTO(
        Long id,
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentTime
) {
    public AppointmentInfosDTO(Appointment appointment) {
        this(appointment.getId(), appointment.getPatient().getId(), appointment.getDoctor().getId(),
                appointment.getAppointmentTime());
    }
}
