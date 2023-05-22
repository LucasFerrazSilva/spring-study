package br.com.ferraz.springstudy.controller;

import br.com.ferraz.springstudy.domain.appointment.ReasonForCancellation;
import jakarta.validation.constraints.NotNull;

public record CancelAppointmentDTO (
        @NotNull
        Long id,
        @NotNull(message="Favor informar o motivo do cancelamento (PATIENT_WITHDREW, DOCTOR_REQUESTED, OTHER)")
        ReasonForCancellation reasonForCancellation
) {}
