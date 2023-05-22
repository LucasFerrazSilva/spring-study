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
}
