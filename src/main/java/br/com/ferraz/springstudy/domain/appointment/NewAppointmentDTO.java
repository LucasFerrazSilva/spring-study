package br.com.ferraz.springstudy.domain.appointment;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NewAppointmentDTO(
        @NotNull
        Long patientId,
        Long doctorId,
        @NotNull
        LocalDateTime appointmentTime
) {
}
