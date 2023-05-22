package br.com.ferraz.springstudy.domain.appointment.validations;

import br.com.ferraz.springstudy.domain.appointment.NewAppointmentDTO;
import br.com.ferraz.springstudy.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class AppointmentTimeInsideOpeningHoursValidator implements NewAppointmentValidator {

    public void validate(NewAppointmentDTO dto) {
        boolean sunday = dto.appointmentTime().getDayOfWeek().equals(DayOfWeek.SUNDAY);
        boolean beforeOpeningHours = dto.appointmentTime().getHour() < 7;
        boolean afterOpeningHours = dto.appointmentTime().getHour() > 18;

        if (sunday || beforeOpeningHours || afterOpeningHours)
            throw new ValidationException("appointmentTime", "Dia/Horário inválido (somente de segunda à sábado das 7 às 19)");
    }

}
