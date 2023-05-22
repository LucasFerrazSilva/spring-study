package br.com.ferraz.springstudy.domain.appointment.validations;

import br.com.ferraz.springstudy.domain.appointment.NewAppointmentDTO;

public interface NewAppointmentValidator {

    void validate(NewAppointmentDTO dto);

}
