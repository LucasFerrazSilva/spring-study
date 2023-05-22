package br.com.ferraz.springstudy.domain.appointment;

import br.com.ferraz.springstudy.domain.doctor.DoctorInfosDTO;
import br.com.ferraz.springstudy.domain.patient.PatientInfosDTO;

import java.time.LocalDateTime;

public record AppointmentInfosDTO(
        Long id,
        PatientInfosDTO patient,
        DoctorInfosDTO doctor,
        LocalDateTime appointmentTime
) {
    public AppointmentInfosDTO(Appointment appointment) {
        this(appointment.getId(), new PatientInfosDTO(appointment.getPatient()),
                new DoctorInfosDTO(appointment.getDoctor()), appointment.getAppointmentTime());
    }
}
