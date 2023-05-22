package br.com.ferraz.springstudy.domain.appointment;

import br.com.ferraz.springstudy.domain.doctor.Expertise;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NewAppointmentDTO(
        @NotNull
        Long patientId,
        Long doctorId,
        Expertise expertise,
        @NotNull
        @Future
        LocalDateTime appointmentTime
) {
        public NewAppointmentDTO(Long patientId, Long doctorId, Expertise expertise, LocalDateTime appointmentTime) {
                this.patientId = patientId;
                this.doctorId = doctorId;
                this.expertise = expertise;
                this.appointmentTime = (appointmentTime != null ? appointmentTime.withMinute(0).withSecond(0) : null);
        }
}
