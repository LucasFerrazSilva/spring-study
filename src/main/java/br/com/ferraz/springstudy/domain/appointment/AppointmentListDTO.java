package br.com.ferraz.springstudy.domain.appointment;

import java.time.LocalDateTime;

public record AppointmentListDTO(
        String patientName,
        String doctorName,
        String doctorExpertise,
        LocalDateTime appointmentTime
) {
    public AppointmentListDTO(Appointment appointment) {
        this(appointment.getPatientName(), appointment.getDoctorName(), appointment.getDoctorExpertise(), appointment.getAppointmentTime());
    }
}
