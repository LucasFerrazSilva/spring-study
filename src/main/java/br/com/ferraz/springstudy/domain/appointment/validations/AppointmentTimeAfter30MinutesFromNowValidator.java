package br.com.ferraz.springstudy.domain.appointment.validations;

import br.com.ferraz.springstudy.domain.appointment.NewAppointmentDTO;
import br.com.ferraz.springstudy.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class AppointmentTimeAfter30MinutesFromNowValidator implements NewAppointmentValidator {

    public void validate(NewAppointmentDTO dto) {
        long minutesUntilAppointment = Duration.between(LocalDateTime.now(), dto.appointmentTime()).toMinutes();

        if (minutesUntilAppointment < 30)
            throw new ValidationException("appointmentTime", "Não é possível realizar o agendamento com menos de 30 minutos de antecedência.");
    }

}
